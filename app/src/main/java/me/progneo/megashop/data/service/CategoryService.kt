package me.progneo.megashop.data.service

import retrofit2.Response
import retrofit2.http.GET

internal interface CategoryService {

    @GET("products/categories")
    suspend fun getCategoryList(): Response<List<String>>
}
