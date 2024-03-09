package me.progneo.megashop.data.service

import me.progneo.megashop.data.model.ProductListResponse
import me.progneo.megashop.domain.entities.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface ProductService {

    @GET("products")
    suspend fun getProductList(
        @Query("skip") skip: Int,
        @Query("limit") limit: Int
    ): Response<ProductListResponse>

    @GET("products/{id}")
    suspend fun getProduct(
        @Path("id") id: Int = 0
    ): Response<Product>

    @GET("products/search")
    suspend fun getProductListByTitle(
        @Query("skip") skip: Int,
        @Query("limit") limit: Int,
        @Query("q") title: String
    ): Response<ProductListResponse>
}
