package me.progneo.megashop.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.progneo.megashop.data.service.CategoryService
import me.progneo.megashop.data.service.ProductService
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
internal object ServiceModule {

    @Provides
    fun providerProductService(retrofit: Retrofit): ProductService =
        retrofit.create(ProductService::class.java)

    @Provides
    fun providerCategoryService(retrofit: Retrofit): CategoryService =
        retrofit.create(CategoryService::class.java)
}
