package me.progneo.megashop.ui.screen.product

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
import me.progneo.megashop.data.enum.PageStatus
import me.progneo.megashop.data.exception.NoConnectivityException
import me.progneo.megashop.domain.entities.Product
import me.progneo.megashop.domain.usecase.GetProductUseCase

@HiltViewModel
class ProductViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getProductUseCase: GetProductUseCase
) : ViewModel() {

    private val _productIdString: String = checkNotNull(savedStateHandle["productId"])

    private val _product = MutableStateFlow<Product?>(null)
    val product = _product.asStateFlow()

    private val _pageStatus = MutableStateFlow(PageStatus.None)
    val pageStatus = _pageStatus.asStateFlow()

    fun fetchProduct() {
        viewModelScope.launch {
            try {
                val productId = _productIdString.toInt()

                _pageStatus.tryEmit(PageStatus.Loading)

                withContext(Dispatchers.IO) {
                    try {
                        getProductUseCase(productId).let { result ->
                            result.getOrNull()?.let { product ->
                                _product.tryEmit(product)
                                _pageStatus.tryEmit(PageStatus.Complete)
                            } ?: {
                                _pageStatus.tryEmit(PageStatus.Error)
                            }
                        }
                    } catch (ex: NoConnectivityException) {
                        _pageStatus.tryEmit(PageStatus.NetworkUnavailable)
                    } catch (ex: Exception) {
                        _pageStatus.tryEmit(PageStatus.Error)
                    }
                }
            } catch (ex: ClassCastException) {
                _pageStatus.tryEmit(PageStatus.Error)
            }
        }
    }
}
