package me.progneo.megashop.ui.screen.category.list

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.progneo.megashop.domain.usecase.GetCategoryListUseCase
import me.progneo.megashop.ui.screen.BaseViewModel

@HiltViewModel
class CategoryListViewModel @Inject constructor(
    private val getCategoryListUseCase: GetCategoryListUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<CategoryListUiState>(CategoryListUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun fetchCategoryList() {
        call(
            useCaseCall = { getCategoryListUseCase() },
            onSuccess = { categoryList ->
                _uiState.tryEmit(CategoryListUiState.Success(categoryList))
            },
            onError = {
                _uiState.tryEmit(CategoryListUiState.Error)
            },
            onNetworkUnavailable = {
                _uiState.tryEmit(CategoryListUiState.NetworkUnavailable)
            },
            onTimeout = {
                _uiState.tryEmit(CategoryListUiState.Error)
            }
        )
    }
}
