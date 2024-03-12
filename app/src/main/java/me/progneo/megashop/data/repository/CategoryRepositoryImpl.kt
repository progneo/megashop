package me.progneo.megashop.data.repository

import java.net.HttpURLConnection
import javax.inject.Inject
import me.progneo.megashop.data.exception.RequestException
import me.progneo.megashop.data.service.CategoryService
import me.progneo.megashop.domain.entities.Category
import me.progneo.megashop.domain.repository.CategoryRepository

internal class CategoryRepositoryImpl @Inject constructor(
    private val categoryService: CategoryService
) : CategoryRepository {

    override suspend fun getCategoryList(): Result<List<Category>> {
        val response = categoryService.getCategoryList()

        if (!response.isSuccessful) {
            return Result.failure(
                RequestException(

                    code = response.code(),
                    message = response.message()
                )
            )
        }

        val categoryListResponse = response.body()

        if (categoryListResponse != null) {
            val categoryList =
                categoryListResponse.map { category: String -> Category(category) }
            return Result.success(categoryList)
        }

        return Result.failure(
            RequestException(
                code = HttpURLConnection.HTTP_INTERNAL_ERROR,
                message = "An error occurred!"
            )
        )
    }
}
