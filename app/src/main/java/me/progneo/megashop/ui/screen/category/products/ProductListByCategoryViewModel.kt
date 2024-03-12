package me.progneo.megashop.ui.screen.category.products

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.progneo.megashop.domain.entities.Product
import me.progneo.megashop.domain.usecase.GetProductListByCategoryUseCase
import me.progneo.megashop.ui.component.navigation.NavArguments
import me.progneo.megashop.ui.component.product.list.ProductListUiState
import me.progneo.megashop.ui.screen.BaseViewModel

@HiltViewModel
class ProductListByCategoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getProductListByCategoryUseCase: GetProductListByCategoryUseCase
) : BaseViewModel() {

    private val _categoryString: String = checkNotNull(savedStateHandle[NavArguments.CATEGORY])
    private val _category = MutableStateFlow(_categoryString)
    val category = _category.asStateFlow()

    private val _productList = MutableStateFlow<List<Product>>(listOf())
    val productList = _productList.asStateFlow()

    private val _uiState = MutableStateFlow<ProductListUiState>(ProductListUiState.Waiting)
    val uiState = _uiState.asStateFlow()

    private var _currentPage = MutableStateFlow(0)

    fun fetchProductList() {
        if (_uiState.value is ProductListUiState.Success ||
            _uiState.value is ProductListUiState.Waiting
        ) {
            _uiState.tryEmit(ProductListUiState.Loading)
            call(
                useCaseCall = {
                    getProductListByCategoryUseCase(
                        skip = PAGE_SIZE * _currentPage.value,
                        limit = PAGE_SIZE,
                        category = _category.value
                    )
                },
                onSuccess = { productList ->
                    if (productList.isNotEmpty()) {
                        _productList.tryEmit(_productList.value + productList)
                        _currentPage.tryEmit(_currentPage.value + 1)
                    }
                    _uiState.tryEmit(ProductListUiState.Success)
                },
                onError = {
                    _uiState.tryEmit(ProductListUiState.Error)
                },
                onNetworkUnavailable = {
                    _uiState.tryEmit(ProductListUiState.NetworkUnavailable)
                },
                onTimeout = {
                    _uiState.tryEmit(ProductListUiState.Error)
                }
            )
        }
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}
