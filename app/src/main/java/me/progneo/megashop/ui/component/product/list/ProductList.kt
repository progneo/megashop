package me.progneo.megashop.ui.component.product.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import me.progneo.megashop.domain.entities.Product
import me.progneo.megashop.ui.component.product.ProductCard
import me.progneo.megashop.ui.util.OnBottomReached
import me.progneo.megashop.ui.util.shimmerEffect

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
                repeat(2) {
                    item {
                        Box(
                            modifier = Modifier
                                .height(290.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .shimmerEffect()
                        )
                    }
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}
