package me.progneo.megashop.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.progneo.megashop.data.enum.PageStatus
import me.progneo.megashop.data.exception.NoConnectivityException
import me.progneo.megashop.domain.entities.Product
import me.progneo.megashop.domain.usecase.GetProductListUseCase

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getProductListUseCase: GetProductListUseCase
) : ViewModel() {

    private val _productList = MutableStateFlow<List<Product>>(listOf())
    val productList = _productList.asStateFlow()

    private val _pageStatus = MutableStateFlow(PageStatus.None)
    val pageStatus = _pageStatus.asStateFlow()

    private var _currentPage = 0

    fun fetchProductList() {
        viewModelScope.launch {
            if (_pageStatus.value == PageStatus.Complete || _pageStatus.value == PageStatus.None) {
                if (_currentPage == 0) {
                    _pageStatus.tryEmit(PageStatus.FirstLoading)
                } else {
                    _pageStatus.tryEmit(PageStatus.Loading)
                }

                withContext(Dispatchers.IO) {
                    try {
                        getProductListUseCase(
                            skip = PAGE_SIZE * _currentPage,
                            limit = PAGE_SIZE
                        ).let { result ->
                            result.getOrNull()?.let { productList ->
                                if (productList.isNotEmpty()) {
                                    _productList.tryEmit(_productList.value + productList)
                                    _currentPage += 1
                                    _pageStatus.tryEmit(PageStatus.Complete)
                                }
                            }
                        }
                    } catch (ex: NoConnectivityException) {
                        _pageStatus.tryEmit(PageStatus.NetworkUnavailable)
                    } catch (ex: Exception) {
                        _pageStatus.tryEmit(PageStatus.Error)
                    }
                }
            }
        }
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}
