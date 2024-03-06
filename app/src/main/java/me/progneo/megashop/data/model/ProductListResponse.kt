package me.progneo.megashop.data.model

import me.progneo.megashop.domain.entities.Product

data class ProductListResponse(
    val products: List<Product>,
    val total: Int,
    val skip: Int,
    val limit: Int
)
