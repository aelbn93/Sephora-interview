package com.interview.sephora.core.database.di

import com.interview.sephora.core.database.SephoraDatabase
import com.interview.sephora.core.database.dao.ProductDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {

    @Provides
    fun providesProductDao(
        database: SephoraDatabase,
    ): ProductDao = database.productDao()

}
