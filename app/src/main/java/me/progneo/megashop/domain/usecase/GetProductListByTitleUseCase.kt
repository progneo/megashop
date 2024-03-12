package me.progneo.megashop.domain.usecase

import javax.inject.Inject
import me.progneo.megashop.domain.entities.Product
import me.progneo.megashop.domain.repository.ProductRepository

class GetProductListByTitleUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {

    suspend operator fun invoke(skip: Int, limit: Int, title: String): Result<List<Product>> {
        return productRepository.getProductList(skip = skip, limit = limit, title = title)
    }
}
