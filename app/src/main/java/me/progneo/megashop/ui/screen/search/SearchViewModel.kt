package me.progneo.megashop.ui.screen.search

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.progneo.megashop.domain.entities.Product
import me.progneo.megashop.domain.usecase.GetProductListByTitleUseCase
import me.progneo.megashop.ui.component.product.list.ProductListUiState
import me.progneo.megashop.ui.screen.BaseViewModel

@HiltViewModel
class SearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getProductListByTitleUseCase: GetProductListByTitleUseCase
) : BaseViewModel() {

    private val _productList = MutableStateFlow<List<Product>>(listOf())
    val productList = _productList.asStateFlow()

    private val _uiState = MutableStateFlow<ProductListUiState>(ProductListUiState.Waiting)
    val uiState = _uiState.asStateFlow()

    private var _searchQuery = MutableStateFlow(savedStateHandle["searchQuery"] ?: "")
    val searchQuery = _searchQuery.asStateFlow()

    private var _currentPage = 0

    init {
        if (_searchQuery.value != "") {
            fetchProductList()
        }
    }

    fun fetchProductList() {
        if (_uiState.value !is ProductListUiState.Loading) {
            _uiState.tryEmit(ProductListUiState.Loading)
            call(
                useCaseCall = {
                    getProductListByTitleUseCase(
                        skip = PAGE_SIZE * _currentPage,
                        limit = PAGE_SIZE,
                        title = _searchQuery.value
                    )
                },
                onSuccess = { productList ->
                    if (productList.isNotEmpty()) {
                        _productList.tryEmit(_productList.value + productList)
                        _currentPage += 1
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

    fun setSearchQuery(query: String) {
        _currentPage = 0
        _searchQuery.tryEmit(query)
        _productList.tryEmit(listOf())
        fetchProductList()
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}
