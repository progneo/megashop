package me.progneo.megashop.ui.screen.product

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import me.progneo.megashop.data.enum.PageStatus
import me.progneo.megashop.domain.entities.Product
import me.progneo.megashop.ui.component.NoInternetConnectionPanel
import me.progneo.megashop.ui.component.UnexpectedErrorPanel
import me.progneo.megashop.ui.util.AnimatedVisibility

@Composable
fun ProductScreen(
    navController: NavController,
    viewModel: ProductViewModel = hiltViewModel()
) {
    val product by viewModel.product.collectAsState()
    val pageStatus by viewModel.pageStatus.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchProduct()
    }

    ProductScreen(
        product = product,
        pageStatus = pageStatus,
        onReload = viewModel::fetchProduct
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    product: Product?,
    pageStatus: PageStatus,
    onReload: () -> Unit
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
                        onReloadClick = onReload,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                AnimatedVisibility(visible = pageStatus == PageStatus.Loading) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }

                AnimatedVisibility(visible = pageStatus == PageStatus.Error) {
                    UnexpectedErrorPanel()
                }

                AnimatedVisibility(visible = product != null && pageStatus == PageStatus.Complete) {
                    ProductInfo(product = product!!)
                }
            }
        }
    }
}

@Composable
fun ProductInfo(product: Product) {
}
