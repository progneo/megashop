package me.progneo.megashop.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import me.progneo.megashop.data.repository.CategoryRepositoryImpl
import me.progneo.megashop.data.repository.ProductRepositoryImpl
import me.progneo.megashop.domain.repository.CategoryRepository
import me.progneo.megashop.domain.repository.ProductRepository

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    @Singleton
    internal abstract fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ): ProductRepository

    @Binds
    @Singleton
    internal abstract fun bindCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ): CategoryRepository
}
