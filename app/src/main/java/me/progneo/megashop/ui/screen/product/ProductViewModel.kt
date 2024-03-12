package me.progneo.megashop.ui.screen.product

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.progneo.megashop.domain.usecase.GetProductUseCase
import me.progneo.megashop.ui.screen.BaseViewModel

@HiltViewModel
class ProductViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getProductUseCase: GetProductUseCase
) : BaseViewModel() {

    private val _productIdString: String = checkNotNull(savedStateHandle["productId"])

    private val _uiState = MutableStateFlow<ProductUiState>(ProductUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun fetchProduct() {
        try {
            val productId = _productIdString.toInt()

            call(
                useCaseCall = {
                    getProductUseCase(productId)
                },
                onSuccess = { product ->
                    _uiState.tryEmit(ProductUiState.Success(product))
                },
                onError = {
                    _uiState.tryEmit(ProductUiState.Error)
                },
                onNetworkUnavailable = {
                    _uiState.tryEmit(ProductUiState.NetworkUnavailable)
                },
                onTimeout = {
                    _uiState.tryEmit(ProductUiState.Error) // todo: add state with timeout probably
                }
            )
        } catch (ex: NumberFormatException) {
            _uiState.tryEmit(ProductUiState.Error)
        }
    }
}
