package me.progneo.megashop.ui.screen.product

import me.progneo.megashop.domain.entities.Product

sealed class ProductUiState {

    data object Loading : ProductUiState()
    data object NetworkUnavailable : ProductUiState()
    data class Success(
        val product: Product
    ) : ProductUiState()

    data object Error : ProductUiState()
}
