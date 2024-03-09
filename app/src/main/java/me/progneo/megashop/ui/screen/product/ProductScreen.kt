package me.progneo.megashop.ui.screen.product

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import me.progneo.megashop.R
import me.progneo.megashop.data.enum.PageStatus
import me.progneo.megashop.domain.entities.Product
import me.progneo.megashop.ui.component.NoInternetConnectionPanel
import me.progneo.megashop.ui.component.RatingBlock
import me.progneo.megashop.ui.component.UnexpectedErrorPanel
import me.progneo.megashop.ui.util.AnimatedVisibility
import me.progneo.megashop.ui.util.provider.SampleProductProvider
import me.progneo.megashop.ui.util.shimmerEffect

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
        onReload = viewModel::fetchProduct,
        onClickReturn = navController::popBackStack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    product: Product?,
    pageStatus: PageStatus,
    onReload: () -> Unit,
    onClickReturn: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {},
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = onClickReturn) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(
                                R.string.button_return_to_main_screen_button
                            )
                        )
                    }
                }
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

                // todo: remove two states and make one with loading in product info
            }
        }
    }
}

@Composable
fun ProductInfo(product: Product) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            ImageCarousel(imageUrlList = product.images)
        }
        item {
            Text(
                text = product.brand,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = product.title,
                style = MaterialTheme.typography.titleLarge
            )
        }
        item {
            Row(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(6.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically

            ) {
                RatingBlock(rating = product.rating, size = 24.dp)
                Text(text = product.rating.toString(), style = MaterialTheme.typography.labelLarge)
            }
        }
        item {
            if (product.stock == 0) {
                Text(
                    text = "Out of stock",
                    style = MaterialTheme.typography.titleLarge
                )
            } else {
                Column {
                    if (product.discountPercentage != 0f) {
                        PriceWithDiscount(
                            price = product.price,
                            discountPercentage = product.discountPercentage
                        )
                    } else {
                        Text(
                            text = "${product.price} $",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    Text(
                        text = "In stock: ${product.stock} pcs.",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
        item {
            Column {
                Text(
                    text = "Description:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ImageCarousel(imageUrlList: List<String>, modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { imageUrlList.size })

    Column {
        HorizontalPager(
            state = pagerState,
            modifier = modifier
                .fillMaxWidth()
        ) { page ->
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        val pageOffset = (
                            pagerState.currentPage - page + pagerState
                                .currentPageOffsetFraction
                            ).absoluteValue

                        lerp(
                            start = 0.85f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        ).also { scale ->
                            scaleX = scale
                            scaleY = scale
                        }

                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }
            ) {
                SubcomposeAsyncImage(
                    model = imageUrlList[page],
                    contentDescription = stringResource(R.string.image_of_product),
                    contentScale = ContentScale.Fit,
                    loading = {
                        Box(
                            modifier = Modifier.fillMaxSize()
                                .clip(RoundedCornerShape(16.dp)).shimmerEffect()
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
            }
        }

        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                    }
                Box(
                    modifier = Modifier
                        .padding(3.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp)
                )
            }
        }
    }
}

@Composable
private fun PriceWithDiscount(price: Int, discountPercentage: Float) {
    val newPrice = (price * (1 - discountPercentage / 100))

    Column {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = String.format("%.1f $", newPrice),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "$price $",
                style = MaterialTheme.typography.bodyLarge.copy(
                    textDecoration = TextDecoration.LineThrough
                ),
                color = MaterialTheme.colorScheme.secondary
            )
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(3.dp)
            ) {
                Text(
                    text = "-${discountPercentage.roundToInt()}%",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Preview(name = "Product screen")
@Composable
fun PreviewProductScreen(
    @PreviewParameter(SampleProductProvider::class) product: Product
) {
    ProductScreen(
        product = product,
        pageStatus = PageStatus.Complete,
        onReload = {},
        onClickReturn = {}
    )
}
