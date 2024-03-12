package me.progneo.megashop.ui.screen.category.products

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import me.progneo.megashop.domain.entities.Product
import me.progneo.megashop.ui.component.IconPanel
import me.progneo.megashop.ui.component.navigation.NavDestinations
import me.progneo.megashop.ui.component.product.list.ProductList
import me.progneo.megashop.ui.component.product.list.ProductListUiState
import me.progneo.megashop.ui.theme.MegaShopTheme
import me.progneo.megashop.ui.util.AnimatedVisibility
import me.progneo.megashop.ui.util.provider.SampleProductProvider

@Composable
fun ProductListByCategoryScreen(
    navController: NavController,
    viewModel: ProductListByCategoryViewModel = hiltViewModel()
) {
    val productList by viewModel.productList.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val category by viewModel.category.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchProductList()
    }

    ProductListByCategoryScreen(
        category = category,
        productList = productList,
        uiState = uiState,
        onLoadMore = viewModel::fetchProductList,
        onProductClick = { navController.navigate("${NavDestinations.PRODUCT_SCREEN}/$it") },
        onReturnClick = navController::popBackStack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListByCategoryScreen(
    category: String,
    productList: List<Product>,
    uiState: ProductListUiState,
    onLoadMore: () -> Unit,
    onProductClick: (Int) -> Unit,
    onReturnClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        category.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase() else it.toString()
                        }.replace('-', ' ')
                    )
                },
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
                AnimatedVisibility(
                    visible = uiState is ProductListUiState.Success ||
                        uiState is ProductListUiState.Loading
                ) {
                    ProductList(
                        productList = productList,
                        isLoading = uiState is ProductListUiState.Loading,
                        onLoadMore = onLoadMore,
                        onProductClick = onProductClick
                    )
                }

                AnimatedVisibility(
                    visible = productList.isEmpty() && uiState is ProductListUiState.Success
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        IconPanel(
                            iconPainter = painterResource(R.drawable.search_off),
                            text = stringResource(R.string.nothing_was_found),
                            modifier = Modifier.fillMaxSize(),
                            content = {
                                FilledTonalButton(
                                    content = {
                                        Text(stringResource(R.string.button_refresh))
                                    },
                                    onClick = onLoadMore
                                )
                            }
                        )
                    }
                }

                AnimatedVisibility(visible = uiState is ProductListUiState.NetworkUnavailable) {
                    IconPanel(
                        iconPainter = painterResource(R.drawable.wifi_off),
                        text = stringResource(R.string.no_internet_connection),
                        modifier = Modifier.fillMaxSize(),
                        content = {
                            FilledTonalButton(
                                content = {
                                    Text(stringResource(R.string.button_refresh))
                                },
                                onClick = onLoadMore
                            )
                        }
                    )
                }

                AnimatedVisibility(visible = uiState is ProductListUiState.Error) {
                    IconPanel(
                        iconPainter = painterResource(R.drawable.broken_image),
                        text = stringResource(R.string.something_went_wrong),
                        modifier = Modifier.fillMaxSize(),
                        content = {
                            FilledTonalButton(
                                content = {
                                    Text(stringResource(R.string.button_refresh))
                                },
                                onClick = onLoadMore
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
fun PreviewProductListByCategoryScreen(
    @PreviewParameter(SampleProductProvider::class) product: Product
) {
    val productList = listOf(product, product, product, product, product, product, product)

    MegaShopTheme {
        ProductListByCategoryScreen(
            category = product.category,
            productList = productList,
            uiState = ProductListUiState.Success,
            onLoadMore = {},
            onProductClick = {},
            onReturnClick = {}
        )
    }
}
