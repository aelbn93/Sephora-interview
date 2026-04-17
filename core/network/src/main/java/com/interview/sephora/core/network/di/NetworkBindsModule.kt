package com.interview.sephora.core.network.di

import com.interview.sephora.core.network.SephoraNetworkDataSource
import com.interview.sephora.core.network.retrofit.RetrofitSephoraNetwork
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface NetworkBindsModule {

    @Binds
    fun bindsNetworkDataSource(
        impl: RetrofitSephoraNetwork
    ): SephoraNetworkDataSource
}