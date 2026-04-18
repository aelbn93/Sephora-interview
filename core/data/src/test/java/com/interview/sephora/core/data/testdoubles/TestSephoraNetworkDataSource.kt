package com.interview.sephora.core.data.testdoubles

import com.interview.sephora.core.network.SephoraNetworkDataSource
import com.interview.sephora.core.network.model.NetworkProduct
import com.interview.sephora.core.network.model.NetworkProductReview

/**
 * Test double for [SephoraNetworkDataSource]
 */
class TestSephoraNetworkDataSource : SephoraNetworkDataSource {

    private var products: List<NetworkProduct> = emptyList()
    private var reviews: List<NetworkProductReview> = emptyList()

    fun setProducts(products: List<NetworkProduct>) {
        this.products = products
    }

    fun setReviews(reviews: List<NetworkProductReview>) {
        this.reviews = reviews
    }

    override suspend fun fetchProducts(): List<NetworkProduct> = products

    override suspend fun fetchProductReviews(): List<NetworkProductReview> = reviews
}