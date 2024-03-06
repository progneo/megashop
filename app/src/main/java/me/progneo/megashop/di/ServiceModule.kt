package me.progneo.megashop.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.progneo.megashop.data.service.ProductService
import retrofit2.Retrofit
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
internal object ServiceModule {

    @Provides
    fun providerProductService(retrofit: Retrofit): ProductService =
        retrofit.create(ProductService::class.java)
}
