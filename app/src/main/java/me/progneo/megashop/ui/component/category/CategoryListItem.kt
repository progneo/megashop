package me.progneo.megashop.ui.component.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import me.progneo.megashop.R
import me.progneo.megashop.domain.entities.Category
import me.progneo.megashop.ui.theme.MegaShopTheme
import me.progneo.megashop.ui.util.provider.SampleCategoryProvider

@Composable
fun CategoryListItem(
    category: Category,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = category.name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase() else it.toString()
                }.replace('-', ' '),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = stringResource(R.string.icon_arrow_right),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        HorizontalDivider()
    }
}

@PreviewLightDark
@Composable
fun PreviewCategoryListItem(
    @PreviewParameter(SampleCategoryProvider::class) category: Category
) {
    MegaShopTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(25.dp)
        ) {
            CategoryListItem(
                category = category,
                onClick = {}
            )
        }
    }
}
