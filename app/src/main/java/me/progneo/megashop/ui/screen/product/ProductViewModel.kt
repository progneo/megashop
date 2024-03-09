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

    private val _uiState = MutableStateFlow<ProductUiState>(ProductUiState.Waiting)
    val uiState = _uiState.asStateFlow()

    fun fetchProduct() {
        viewModelScope.launch {
            try {
                val productId = _productIdString.toInt()

                _uiState.tryEmit(ProductUiState.Loading)

                withContext(Dispatchers.IO) {
                    try {
                        getProductUseCase(productId).let { result ->
                            result.getOrNull()?.let { product ->
                                _product.tryEmit(product)
                                _uiState.tryEmit(ProductUiState.Success)
                            } ?: {
                                _uiState.tryEmit(ProductUiState.Error)
                            }
                        }
                    } catch (ex: NoConnectivityException) {
                        _uiState.tryEmit(ProductUiState.NetworkUnavailable)
                    } catch (ex: Exception) {
                        _uiState.tryEmit(ProductUiState.Error)
                    }
                }
            } catch (ex: ClassCastException) {
                _uiState.tryEmit(ProductUiState.Error)
            }
        }
    }
}
