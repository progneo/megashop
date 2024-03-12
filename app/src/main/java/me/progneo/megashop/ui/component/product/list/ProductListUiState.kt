package me.progneo.megashop.ui.component.product.list

sealed class ProductListUiState {

    data object Waiting : ProductListUiState()
    data object Loading : ProductListUiState()
    data object NetworkUnavailable : ProductListUiState()
    data object Success : ProductListUiState()
    data object Error : ProductListUiState()
}
