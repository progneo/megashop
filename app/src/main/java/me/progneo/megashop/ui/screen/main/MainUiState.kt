package me.progneo.megashop.ui.screen.main

sealed class MainUiState {

    data object Waiting : MainUiState()
    data object Loading : MainUiState()
    data object NetworkUnavailable : MainUiState()
    data object Success : MainUiState()
    data object Error : MainUiState()
}
