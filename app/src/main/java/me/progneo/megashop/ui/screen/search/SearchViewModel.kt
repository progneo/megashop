package me.progneo.megashop.ui.screen.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.progneo.megashop.data.exception.NoConnectivityException
import me.progneo.megashop.domain.entities.Product
import me.progneo.megashop.domain.usecase.GetProductListByTitleUseCase

@HiltViewModel
class SearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getProductListByTitleUseCase: GetProductListByTitleUseCase
) : ViewModel() {

    private val _productList = MutableStateFlow<List<Product>>(listOf())
    val productList = _productList.asStateFlow()

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Waiting)
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
        viewModelScope.launch {
            if (_uiState.value == SearchUiState.Success ||
                _uiState.value == SearchUiState.Waiting
            ) {
                _uiState.tryEmit(SearchUiState.Loading)

                withContext(Dispatchers.IO) {
                    try {
                        getProductListByTitleUseCase(
                            skip = PAGE_SIZE * _currentPage,
                            limit = PAGE_SIZE,
                            title = _searchQuery.value
                        ).let { result ->
                            result.getOrNull()?.let { productList ->
                                if (productList.isNotEmpty()) {
                                    _productList.tryEmit(_productList.value + productList)
                                    _currentPage += 1
                                }
                            }
                            _uiState.tryEmit(SearchUiState.Success)
                        }
                    } catch (ex: NoConnectivityException) {
                        _uiState.tryEmit(SearchUiState.NetworkUnavailable)
                    } catch (ex: Exception) {
                        _uiState.tryEmit(SearchUiState.Error)
                    }
                }
            }
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
