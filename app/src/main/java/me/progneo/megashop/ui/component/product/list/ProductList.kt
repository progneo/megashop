package me.progneo.megashop.ui.component.product.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.progneo.megashop.domain.entities.Product
import me.progneo.megashop.ui.component.product.ProductCard
import me.progneo.megashop.ui.component.product.ProductCardPlaceholder
import me.progneo.megashop.ui.util.OnBottomReached

@Composable
fun ProductList(
    productList: List<Product>,
    isLoading: Boolean,
    onLoadMore: () -> Unit,
    onProductClick: (Int) -> Unit
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
                ProductCard(
                    product = product,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onProductClick(product.id) }
                )
            }
            if (isLoading) {
                repeat(if (productList.size % 2 == 1) 1 else 2) {
                    item {
                        ProductCardPlaceholder()
                    }
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}
