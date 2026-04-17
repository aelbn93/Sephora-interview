package com.interview.sephora.core.network.retrofit

import androidx.tracing.trace
import com.interview.sephora.core.network.SephoraNetworkDataSource
import com.interview.sephora.core.network.model.NetworkProduct
import com.interview.sephora.core.network.model.NetworkProductReview
import dagger.Lazy
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import javax.inject.Inject
import javax.inject.Singleton

private interface RetrofitSephoraNetworkApi {

    @GET(value = "items.json")
    suspend fun getProducts(
    ): List<NetworkProduct>

    @GET(value = "reviews.json")
    suspend fun getProductReview(
    ): List<NetworkProductReview>
}

@Singleton
internal class RetrofitSephoraNetwork @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: Lazy<Call.Factory>,
) : SephoraNetworkDataSource {

    private val networkApi = trace("RetrofitSephoraNetwork") {
        Retrofit.Builder()
            // Fixed for this project. In a multi-environment setup this would be injected
            // via a build variant (e.g. BuildConfig.BASE_URL) or a @Named qualifier.
            .baseUrl("https://sephoraandroid.github.io/testProject/")
            .callFactory { okhttpCallFactory.get().newCall(it) }
            .addConverterFactory(
                networkJson.asConverterFactory("application/json".toMediaType()),
            )
            .build()
            .create(RetrofitSephoraNetworkApi::class.java)
    }

    override suspend fun fetchProducts(): List<NetworkProduct> {
        return networkApi.getProducts()
    }

    override suspend fun fetchProductReviews(): List<NetworkProductReview> {
        return networkApi.getProductReview()
    }
}
