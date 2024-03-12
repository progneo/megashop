package me.progneo.megashop.ui.screen.category.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import me.progneo.megashop.R
import me.progneo.megashop.domain.entities.Category
import me.progneo.megashop.ui.component.IconPanel
import me.progneo.megashop.ui.component.category.CategoryListItem
import me.progneo.megashop.ui.component.category.CategoryListItemPlaceholder
import me.progneo.megashop.ui.component.navigation.NavDestinations
import me.progneo.megashop.ui.theme.MegaShopTheme
import me.progneo.megashop.ui.util.AnimatedVisibility
import me.progneo.megashop.ui.util.provider.SampleCategoryProvider

@Composable
fun CategoryListScreen(
    navController: NavController,
    viewModel: CategoryListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchCategoryList()
    }

    CategoryListScreen(
        uiState = uiState,
        onCategoryClick = {
            navController.navigate("${NavDestinations.PRODUCT_LIST_BY_CATEGORY_SCREEN}/$it")
        },
        onReturnClick = navController::popBackStack,
        onReloadClick = viewModel::fetchCategoryList
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryListScreen(
    uiState: CategoryListUiState,
    onCategoryClick: (String) -> Unit,
    onReturnClick: () -> Unit,
    onReloadClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text("Select category") },
                navigationIcon = {
                    IconButton(onClick = onReturnClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(
                                R.string.button_return_to_previous_screen_button
                            )
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(innerPadding)
        ) {
            Box {
                AnimatedVisibility(visible = uiState is CategoryListUiState.Success) {
                    LazyColumn {
                        items((uiState as CategoryListUiState.Success).categoryList) { category ->
                            CategoryListItem(
                                category = category,
                                onClick = { onCategoryClick(category.name) }
                            )
                        }
                    }
                }

                AnimatedVisibility(visible = uiState is CategoryListUiState.Loading) {
                    Column {
                        repeat(10) {
                            CategoryListItemPlaceholder()
                        }
                    }
                }

                AnimatedVisibility(visible = uiState is CategoryListUiState.NetworkUnavailable) {
                    IconPanel(
                        iconPainter = painterResource(R.drawable.wifi_off),
                        text = stringResource(R.string.no_internet_connection),
                        modifier = Modifier.fillMaxSize(),
                        content = {
                            FilledTonalButton(
                                content = {
                                    Text(stringResource(R.string.button_refresh))
                                },
                                onClick = onReloadClick
                            )
                        }
                    )
                }

                AnimatedVisibility(visible = uiState is CategoryListUiState.Error) {
                    IconPanel(
                        iconPainter = painterResource(R.drawable.broken_image),
                        text = stringResource(R.string.something_went_wrong),
                        modifier = Modifier.fillMaxSize(),
                        content = {
                            FilledTonalButton(
                                content = {
                                    Text(stringResource(R.string.button_refresh))
                                },
                                onClick = onReloadClick
                            )
                        }
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewCategoryListScreen(
    @PreviewParameter(SampleCategoryProvider::class) category: Category
) {
    val categoryList = listOf(category, category, category, category, category, category)

    MegaShopTheme {
        CategoryListScreen(
            uiState = CategoryListUiState.Success(categoryList),
            onCategoryClick = {},
            onReturnClick = {},
            onReloadClick = {}
        )
    }
}
