package me.progneo.megashop.domain.repository

import me.progneo.megashop.domain.entities.Category

interface CategoryRepository {

    suspend fun getCategoryList(): Result<List<Category>>
}
