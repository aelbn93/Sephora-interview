package com.interview.sephora.core.network

import com.interview.sephora.core.network.model.NetworkProduct
import com.interview.sephora.core.network.model.NetworkProductReview

/**
 * Interface representing network calls to the Sephora backend
 */
interface SephoraNetworkDataSource {

    suspend fun fetchProducts(): List<NetworkProduct>

    suspend fun fetchProductReviews(): List<NetworkProductReview>
}
