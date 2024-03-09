package me.progneo.megashop.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import me.progneo.megashop.R
import me.progneo.megashop.ui.theme.MegaShopTheme

@Composable
fun RatingBlock(rating: Float, modifier: Modifier = Modifier, size: Dp = 16.dp) {
    val roundedRating = (rating * 2).roundToInt() / 2f
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Row {
            for (i in 0..4) {
                if (roundedRating - i >= 1) {
                    Icon(
                        modifier = Modifier.size(size),
                        painter = painterResource(R.drawable.star_filled),
                        contentDescription = stringResource(R.string.icon_filled_star),
                        tint = MaterialTheme.colorScheme.primary
                    )
                } else if (roundedRating - i == 0.5f) {
                    Icon(
                        modifier = Modifier.size(size),
                        painter = painterResource(R.drawable.star_half),
                        contentDescription = stringResource(R.string.icon_half_filled_star),
                        tint = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Icon(
                        modifier = Modifier.size(size),
                        painter = painterResource(R.drawable.star_empty),
                        contentDescription = stringResource(R.string.icon_empty_star),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun RatingBlockPreview() {
    MegaShopTheme {
        Box(modifier = Modifier.padding(8.dp)) {
            RatingBlock(rating = 3.33f)
        }
    }
}
