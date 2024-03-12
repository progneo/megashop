package me.progneo.megashop.ui.component.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import kotlin.math.roundToInt
import me.progneo.megashop.R
import me.progneo.megashop.domain.entities.Product
import me.progneo.megashop.ui.theme.MegaShopTheme
import me.progneo.megashop.ui.util.provider.SampleProductProvider
import me.progneo.megashop.ui.util.shimmerEffect
import me.progneo.megashop.ui.util.toDp

@Composable
fun ProductCard(
    product: Product,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .height(290.dp)
    ) {
        Column {
            Box {
                SubcomposeAsyncImage(
                    model = product.thumbnail,
                    contentDescription = product.description,
                    contentScale = ContentScale.Crop,
                    loading = {
                        Box(
                            modifier = Modifier
                                .shimmerEffect()
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                )
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(topEnd = 8.dp, bottomStart = 8.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .padding(4.dp)
                        .align(Alignment.TopEnd),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(R.drawable.star_filled),
                        contentDescription = stringResource(R.string.icon_filled_star),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = product.rating.toString(),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Column {
                    Text(
                        text = product.brand,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.secondary,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Text(
                        text = product.title,
                        style = MaterialTheme.typography.titleMedium,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
                Box {
                    if (product.stock == 0) {
                        Text(
                            text = "Out of stock",
                            style = MaterialTheme.typography.titleMedium
                        )
                    } else {
                        if (product.discountPercentage != 0f) {
                            PriceWithDiscount(
                                price = product.price,
                                discountPercentage = product.discountPercentage
                            )
                        } else {
                            Text(
                                text = "${product.price} $",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PriceWithDiscount(price: Int, discountPercentage: Float) {
    val newPrice = (price * (1 - discountPercentage / 100)).roundToInt()

    Column {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$newPrice $",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                modifier = Modifier.weight(1f),
                text = "$price $",
                style = MaterialTheme.typography.bodySmall.copy(
                    textDecoration = TextDecoration.LineThrough
                ),
                color = MaterialTheme.colorScheme.secondary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(3.dp)
            ) {
                Text(
                    text = "-${discountPercentage.roundToInt()}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun ProductCardPlaceholder() {
    Column {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            ),
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .height(290.dp)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .shimmerEffect()
                )
                Column(
                    modifier = Modifier.padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp, 14.sp.toDp())
                                    .clip(RoundedCornerShape(8.dp))
                                    .shimmerEffect()
                            )
                            Box(
                                modifier = Modifier
                                    .size(150.dp, 16.sp.toDp())
                                    .clip(RoundedCornerShape(8.dp))
                                    .shimmerEffect()
                            )
                        }
                    }
                    Box {
                        Box(
                            modifier = Modifier
                                .size(90.dp, 16.sp.toDp())
                                .clip(RoundedCornerShape(8.dp))
                                .shimmerEffect()
                        )
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewProductCard(
    @PreviewParameter(SampleProductProvider::class) product: Product
) {
    MegaShopTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(25.dp)
        ) {
            ProductCard(product = product)
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewProductCardPlaceholder() {
    MegaShopTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(25.dp)
        ) {
            ProductCardPlaceholder()
        }
    }
}
