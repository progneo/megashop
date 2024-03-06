package me.progneo.megashop.ui.screen.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import me.progneo.megashop.data.enum.PageStatus
import me.progneo.megashop.domain.entities.Product
import me.progneo.megashop.ui.component.NoInternetConnectionPanel
import me.progneo.megashop.ui.component.ProductCard
import me.progneo.megashop.ui.util.AnimatedVisibility
import me.progneo.megashop.ui.util.OnBottomReached
import me.progneo.megashop.ui.util.provider.SampleProductProvider

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val productList by viewModel.productList.collectAsState()
    val pageStatus by viewModel.pageStatus.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchProductList()
    }

    MainScreen(
        productList = productList,
        pageStatus = pageStatus,
        onLoadMore = viewModel::fetchProductList
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    productList: List<Product>,
    pageStatus: PageStatus,
    onLoadMore: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text("title") },
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
                AnimatedVisibility(visible = pageStatus == PageStatus.NetworkUnavailable) {
                    NoInternetConnectionPanel(
                        onReloadClick = onLoadMore,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                AnimatedVisibility(visible = pageStatus == PageStatus.FirstLoading) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }

                AnimatedVisibility(
                    visible = productList.isEmpty() && pageStatus == PageStatus.Complete
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
                    pageStatus == PageStatus.Complete || pageStatus == PageStatus.Loading
                ) {
                    ProductList(
                        productList = productList,
                        onLoadMore = onLoadMore
                    )
                }
            }
        }
    }
}

@Composable
fun ProductList(
    productList: List<Product>,
    onLoadMore: () -> Unit
) {
    val gridState = rememberLazyStaggeredGridState()
    gridState.OnBottomReached(6) {
        onLoadMore()
    }

    LazyVerticalStaggeredGrid(
        state = gridState,
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 16.dp,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 80.dp),
        content = {
            items(productList) { product ->
                ProductCard(product = product, modifier = Modifier.fillMaxWidth())
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Preview(name = "Main screen with products")
@Composable
fun MainScreenPreview(
    @PreviewParameter(SampleProductProvider::class) product: Product
) {
    val productList = listOf(product, product, product, product, product, product, product)

    MainScreen(
        productList = productList,
        pageStatus = PageStatus.Complete,
        onLoadMore = {}
    )
}

@Preview(name = "Main screen network unavailable")
@Composable
fun MainScreenNetworkUnavailablePreview() {
    MainScreen(
        productList = listOf(),
        pageStatus = PageStatus.NetworkUnavailable,
        onLoadMore = {}
    )
}

@Preview(name = "Main screen first loading")
@Composable
fun MainScreenFirstLoadingPreview() {
    MainScreen(
        productList = listOf(),
        pageStatus = PageStatus.FirstLoading,
        onLoadMore = {}
    )
}

@Preview(name = "Main screen empty list")
@Composable
fun MainScreenEmptyListPreview() {
    MainScreen(
        productList = listOf(),
        pageStatus = PageStatus.Complete,
        onLoadMore = {}
    )
}
