package com.interview.sephora.core.data.repository

import com.interview.sephora.core.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProductsWithReviews(): Flow<List<Product>>

    suspend fun sync()

    fun searchProductsWithReviews(query: String): Flow<List<Product>>
}