package me.progneo.megashop.domain.repository

import me.progneo.megashop.domain.entities.Product

interface ProductRepository {

    suspend fun getProductList(
        skip: Int,
        limit: Int,
        title: String? = null
    ): Result<List<Product>>

    suspend fun getProduct(id: Int): Result<Product>
}
