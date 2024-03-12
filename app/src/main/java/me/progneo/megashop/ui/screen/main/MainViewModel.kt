package me.progneo.megashop.ui.screen.main

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.progneo.megashop.domain.entities.Product
import me.progneo.megashop.domain.usecase.GetProductListUseCase
import me.progneo.megashop.ui.component.product.list.ProductListUiState
import me.progneo.megashop.ui.screen.BaseViewModel

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getProductListUseCase: GetProductListUseCase
) : BaseViewModel() {

    private val _productList = MutableStateFlow<List<Product>>(listOf())
    val productList = _productList.asStateFlow()

    private val _uiState = MutableStateFlow<ProductListUiState>(ProductListUiState.Waiting)
    val uiState = _uiState.asStateFlow()

    private var _currentPage = MutableStateFlow(0)

    fun fetchProductList() {
        if (_uiState.value !is ProductListUiState.Loading) {
            _uiState.tryEmit(ProductListUiState.Loading)
            call(
                useCaseCall = {
                    getProductListUseCase(
                        skip = PAGE_SIZE * _currentPage.value,
                        limit = PAGE_SIZE
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
