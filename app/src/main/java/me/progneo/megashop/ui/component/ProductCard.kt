package me.progneo.megashop.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import kotlin.math.roundToInt
import me.progneo.megashop.domain.entities.Product
import me.progneo.megashop.ui.util.provider.SampleProductProvider

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
    ) {
        Column {
            SubcomposeAsyncImage(
                model = product.thumbnail,
                contentDescription = product.description,
                contentScale = ContentScale.Crop,
                loading = {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            )
            Column(
                modifier = Modifier.padding(12.dp),
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
                RatingBlock(rating = product.rating)
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

@Composable
fun PriceWithDiscount(price: Int, discountPercentage: Float) {
    val newPrice = (price * (1 - discountPercentage / 100)).roundToInt()
    Column {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$newPrice $",
                style = MaterialTheme.typography.titleMedium
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
        Text(
            text = "$price $",
            style = MaterialTheme.typography.titleSmall.copy(
                textDecoration = TextDecoration.LineThrough
            ),
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Preview(name = "Product card", showBackground = true)
@Composable
fun ProductCardPreview(
    @PreviewParameter(SampleProductProvider::class) product: Product
) {
    Box(modifier = Modifier.padding(25.dp)) {
        ProductCard(product = product)
    }
}
