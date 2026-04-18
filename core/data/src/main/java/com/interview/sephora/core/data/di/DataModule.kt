package com.interview.sephora.core.data.di

import com.interview.sephora.core.data.repository.DefaultProductRepository
import com.interview.sephora.core.data.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsProductRepository(
        productRepository: DefaultProductRepository,
    ): ProductRepository


}
