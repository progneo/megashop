package me.progneo.megashop.ui.screen.product

sealed class ProductUiState {

    data object Waiting : ProductUiState()
    data object Loading : ProductUiState()
    data object NetworkUnavailable : ProductUiState()
    data object Success : ProductUiState()
    data object Error : ProductUiState()
}
