package me.progneo.megashop.ui.util.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import me.progneo.megashop.domain.entities.Product

class SampleProductProvider : PreviewParameterProvider<Product> {

    override val values = sequenceOf(
        Product(
            id = 1,
            title = "iPhone 9",
            description = "An apple mobile which is nothing like apple",
            price = 549,
            discountPercentage = 12.96f,
            rating = 4.69f,
            stock = 94,
            brand = "Apple",
            category = "smartphones",
            thumbnail = "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg",
            images = listOf(
                "https://cdn.dummyjson.com/product-images/1/1.jpg",
                "https://cdn.dummyjson.com/product-images/1/2.jpg",
                "https://cdn.dummyjson.com/product-images/1/3.jpg",
                "https://cdn.dummyjson.com/product-images/1/4.jpg",
                "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg"
            )
        )
    )
}
