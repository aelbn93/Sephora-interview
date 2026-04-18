package com.interview.sephora.core.testing.repository

import com.interview.sephora.core.data.repository.ProductRepository
import com.interview.sephora.core.model.Product
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import org.jetbrains.annotations.TestOnly

class TestProductRepository : ProductRepository {

    /**
     * The backing hot flow for the list of products for testing.
     */
    private val productsFlow: MutableSharedFlow<List<Product>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun getProductsWithReviews(): Flow<List<Product>> = productsFlow

    override fun searchProductsWithReviews(query: String): Flow<List<Product>> =
        productsFlow.map { products ->
            products.filter { product ->
                product.productName.contains(query, ignoreCase = true)
            }
        }

    override suspend fun sync() {
        // No-op for testing — call sendProducts() to push data manually
    }

    /**
     * A test-only API to emit a list of products into the flow,
     * simulating what the real repository would emit from the database.
     */
    @TestOnly
    fun sendProducts(products: List<Product>) {
        productsFlow.tryEmit(products)
    }

    /**
     * Convenience to get the current count of cached products.
     */
    @TestOnly
    fun getCurrentProducts(): List<Product> = productsFlow.replayCache.firstOrNull() ?: emptyList()
}