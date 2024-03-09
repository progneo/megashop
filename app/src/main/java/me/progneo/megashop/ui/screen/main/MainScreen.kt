package me.progneo.megashop.ui.screen.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import me.progneo.megashop.R
import me.progneo.megashop.domain.entities.Product
import me.progneo.megashop.ui.component.IconPanel
import me.progneo.megashop.ui.component.NoInternetConnectionPanel
import me.progneo.megashop.ui.component.ProductList
import me.progneo.megashop.ui.theme.MegaShopTheme
import me.progneo.megashop.ui.util.AnimatedVisibility
import me.progneo.megashop.ui.util.NavDestinations
import me.progneo.megashop.ui.util.provider.SampleProductProvider

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val productList by viewModel.productList.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchProductList()
    }

    MainScreen(
        productList = productList,
        uiState = uiState,
        onLoadMore = viewModel::fetchProductList,
        onProductClick = { navController.navigate("${NavDestinations.PRODUCT_SCREEN}/$it") },
        onSearchClick = { navController.navigate(NavDestinations.SEARCH_SCREEN) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    productList: List<Product>,
    uiState: MainUiState,
    onLoadMore: () -> Unit,
    onProductClick: (Int) -> Unit,
    onSearchClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text("All products") },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = stringResource(R.string.button_search)
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
                AnimatedVisibility(visible = uiState == MainUiState.NetworkUnavailable) {
                    NoInternetConnectionPanel(
                        onReloadClick = onLoadMore,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                AnimatedVisibility(
                    visible = productList.isEmpty() && uiState == MainUiState.Success
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "Nothing was found",
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                AnimatedVisibility(
                    uiState == MainUiState.Success || uiState == MainUiState.Loading
                ) {
                    ProductList(
                        productList = productList,
                        isLoading = uiState == MainUiState.Loading,
                        onLoadMore = onLoadMore,
                        onProductClick = onProductClick
                    )
                }

                AnimatedVisibility(visible = uiState == MainUiState.Error) {
                    IconPanel(
                        iconPainter = painterResource(R.drawable.broken_image),
                        text = stringResource(R.string.something_went_wrong)
                    )
                }
            }
        }
    }
}

@Preview(name = "Main screen with products")
@Composable
fun MainScreenPreview(
    @PreviewParameter(SampleProductProvider::class) product: Product
) {
    val productList = listOf(product, product, product, product, product, product, product)

    MegaShopTheme {
        MainScreen(
            productList = productList,
            uiState = MainUiState.Success,
            onLoadMore = {},
            onProductClick = {},
            onSearchClick = {}
        )
    }
}

@Preview(name = "Main screen network unavailable")
@Composable
fun MainScreenNetworkUnavailablePreview() {
    MegaShopTheme {
        MainScreen(
            productList = listOf(),
            uiState = MainUiState.NetworkUnavailable,
            onLoadMore = {},
            onProductClick = {},
            onSearchClick = {}
        )
    }
}

@Preview(name = "Main screen empty list")
@Composable
fun MainScreenEmptyListPreview() {
    MegaShopTheme {
        MainScreen(
            productList = listOf(),
            uiState = MainUiState.Success,
            onLoadMore = {},
            onProductClick = {},
            onSearchClick = {}
        )
    }
}
