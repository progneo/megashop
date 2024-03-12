package me.progneo.megashop.domain.usecase

import javax.inject.Inject
import me.progneo.megashop.domain.entities.Category
import me.progneo.megashop.domain.repository.CategoryRepository

class GetCategoryListUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {

    suspend operator fun invoke(): Result<List<Category>> {
        return categoryRepository.getCategoryList()
    }
}
