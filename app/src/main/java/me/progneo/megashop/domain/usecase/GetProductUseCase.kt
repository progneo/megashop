package me.progneo.megashop.domain.usecase

import javax.inject.Inject
import me.progneo.megashop.domain.entities.Product
import me.progneo.megashop.domain.repository.ProductRepository

class GetProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {

    suspend operator fun invoke(id: Int): Result<Product> {
        return productRepository.getProduct(id)
    }
}
