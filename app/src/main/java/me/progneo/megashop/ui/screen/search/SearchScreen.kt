package me.progneo.megashop.ui.screen.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import me.progneo.megashop.R
import me.progneo.megashop.domain.entities.Product
import me.progneo.megashop.ui.component.IconPanel
import me.progneo.megashop.ui.component.NoInternetConnectionPanel
import me.progneo.megashop.ui.component.ProductList
import me.progneo.megashop.ui.component.TextField
import me.progneo.megashop.ui.theme.MegaShopTheme
import me.progneo.megashop.ui.util.AnimatedVisibility
import me.progneo.megashop.ui.util.NavDestinations
import me.progneo.megashop.ui.util.provider.SampleProductProvider

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val productList by viewModel.productList.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    SearchScreen(
        productList = productList,
        uiState = uiState,
        searchQuery = searchQuery,
        onLoadMore = viewModel::fetchProductList,
        onProductClick = { navController.navigate("${NavDestinations.PRODUCT_SCREEN}/$it") },
        onSearchClick = viewModel::setSearchQuery,
        onReturnClick = navController::popBackStack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    productList: List<Product>,
    uiState: SearchUiState,
    searchQuery: String,
    onLoadMore: () -> Unit,
    onProductClick: (Int) -> Unit,
    onSearchClick: (String) -> Unit,
    onReturnClick: () -> Unit
) {
    var searchValue by rememberSaveable { mutableStateOf(searchQuery) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    TextField(
                        value = searchValue,
                        onValueChange = { searchValue = it },
                        placeholder = stringResource(R.string.enter_search_text),
                        keyboardActions = KeyboardActions(onDone = {
                            if (searchValue.isNotBlank()) {
                                onSearchClick(searchValue)
                            }
                        }),
                        tailingIcon = {
                            IconButton(
                                modifier = Modifier
                                    .requiredSize(48.dp)
                                    .offset(x = (12).dp),
                                onClick = { onSearchClick(searchValue) }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.search),
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                    contentDescription = "Search icon" // todo: move to res
                                )
                            }
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onReturnClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(
                                R.string.button_return_to_main_screen_button
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
                AnimatedVisibility(
                    visible = uiState == SearchUiState.NetworkUnavailable
                ) {
                    NoInternetConnectionPanel(
                        onReloadClick = onLoadMore,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                AnimatedVisibility(visible = uiState == SearchUiState.Waiting) {
                    IconPanel(
                        iconPainter = painterResource(R.drawable.search),
                        text = stringResource(R.string.enter_search_text),
                        modifier = Modifier.fillMaxSize()
                    )
                }

                AnimatedVisibility(
                    visible = productList.isEmpty() && uiState == SearchUiState.Success
                ) {
                    IconPanel(
                        iconPainter = painterResource(R.drawable.search_off),
                        text = stringResource(R.string.nothing_was_found),
                        modifier = Modifier.fillMaxSize()
                    )
                }

                AnimatedVisibility(
                    uiState == SearchUiState.Success || uiState == SearchUiState.Loading
                ) {
                    ProductList(
                        productList = productList,
                        isLoading = uiState == SearchUiState.Loading,
                        onLoadMore = onLoadMore,
                        onProductClick = onProductClick
                    )
                }

                AnimatedVisibility(visible = uiState == SearchUiState.Error) {
                    IconPanel(
                        iconPainter = painterResource(R.drawable.broken_image),
                        text = stringResource(R.string.something_went_wrong),
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Preview(name = "Search screen with products")
@Composable
fun SearchScreenPreview(
    @PreviewParameter(SampleProductProvider::class) product: Product
) {
    val productList = listOf(product, product, product, product, product, product, product)
    MegaShopTheme {
        SearchScreen(
            productList = productList,
            uiState = SearchUiState.Success,
            searchQuery = "",
            onLoadMore = {},
            onProductClick = {},
            onSearchClick = {},
            onReturnClick = {}
        )
    }
}

@Preview(name = "Waiting for the search query")
@Composable
fun SearchScreenWaitingPreview() {
    MegaShopTheme {
        SearchScreen(
            productList = listOf(),
            uiState = SearchUiState.Waiting,
            searchQuery = "",
            onLoadMore = {},
            onProductClick = {},
            onSearchClick = {},
            onReturnClick = {}
        )
    }
}

@Preview(name = "Search screen network unavailable")
@Composable
fun SearchScreenNetworkUnavailablePreview() {
    MegaShopTheme {
        SearchScreen(
            productList = listOf(),
            uiState = SearchUiState.NetworkUnavailable,
            searchQuery = "",
            onLoadMore = {},
            onProductClick = {},
            onSearchClick = {},
            onReturnClick = {}
        )
    }
}

@Preview(name = "Search screen empty list")
@Composable
fun SearchScreenEmptyListPreview() {
    MegaShopTheme {
        SearchScreen(
            productList = listOf(),
            uiState = SearchUiState.Success,
            searchQuery = "",
            onLoadMore = {},
            onProductClick = {},
            onSearchClick = {},
            onReturnClick = {}
        )
    }
}
