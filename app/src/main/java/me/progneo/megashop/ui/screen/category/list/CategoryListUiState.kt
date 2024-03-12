package me.progneo.megashop.ui.screen.category.list

import me.progneo.megashop.domain.entities.Category

sealed class CategoryListUiState {

    data object Loading : CategoryListUiState()
    data object NetworkUnavailable : CategoryListUiState()
    data object Error : CategoryListUiState()
    data class Success(
        val categoryList: List<Category>
    ) : CategoryListUiState()
}
