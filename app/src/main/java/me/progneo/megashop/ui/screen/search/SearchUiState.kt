package me.progneo.megashop.ui.screen.search

sealed class SearchUiState {

    data object Waiting : SearchUiState()
    data object Loading : SearchUiState()
    data object NetworkUnavailable : SearchUiState()
    data object Success : SearchUiState()
    data object Error : SearchUiState()
}
