package me.progneo.megashop.data.repository

import java.net.HttpURLConnection
import javax.inject.Inject
import me.progneo.megashop.data.exception.RequestException
import me.progneo.megashop.data.service.ProductService
import me.progneo.megashop.domain.entities.Product
import me.progneo.megashop.domain.repository.ProductRepository

internal class ProductRepositoryImpl @Inject constructor(
    private val productService: ProductService
) : ProductRepository {

    private val cachedList: MutableList<Product> = mutableListOf()

    override suspend fun getProductList(
        skip: Int,
        limit: Int
    ): Result<List<Product>> {
        val response = productService.getProductList(
            skip = skip,
            limit = limit
        )

        if (!response.isSuccessful) {
            return Result.failure(
                RequestException(
                    code = response.code(),
                    message = response.message()
                )
            )
        }

        val productListResponse = response.body()

        if (productListResponse != null) {
            val productList = productListResponse.products
            cachedList.addAll(productList)
            cachedList.distinctBy { it.id }
            return Result.success(productList)
        }

        return Result.failure(
            RequestException(
                code = HttpURLConnection.HTTP_INTERNAL_ERROR,
                message = "An error occurred!"
            )
        )
    }

    override suspend fun getProduct(id: Int): Result<Product> {
        return cachedList.find { it.id == id }?.let { project ->
            Result.success(project)
        } ?: run {
            val response = productService.getProduct(id)

            if (!response.isSuccessful) {
                return Result.failure(
                    RequestException(
                        code = response.code(),
                        message = response.message()
                    )
                )
            }

            val product = response.body()

            if (product != null) {
                return Result.success(product)
            }

            return Result.failure(
                RequestException(
                    code = HttpURLConnection.HTTP_INTERNAL_ERROR,
                    message = "An error occurred!"
                )
            )
        }
    }
}
