package me.progneo.megashop.ui.util.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import me.progneo.megashop.domain.entities.Category

class SampleCategoryProvider : PreviewParameterProvider<Category> {

    override val values = sequenceOf(
        Category(
            name = "smartphones"
        )
    )
}
