package com.interview.sephora.core.data.testdoubles

import com.interview.sephora.core.database.dao.ProductDao
import com.interview.sephora.core.database.model.ProductEntity
import com.interview.sephora.core.database.model.ProductWithReviews
import com.interview.sephora.core.database.model.ReviewEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/**
 * Test double for [ProductDao]
 */
class TestProductDao : ProductDao {

    private val productsStateFlow = MutableStateFlow(emptyList<ProductEntity>())
    private val reviewsStateFlow = MutableStateFlow(emptyList<ReviewEntity>())

    override fun getProductsWithReviews(): Flow<List<ProductWithReviews>> =
        productsStateFlow.map { products ->
            products.map { product ->
                ProductWithReviews(
                    product = product,
                    reviews = reviewsStateFlow.value.filter { it.productId == product.productId },
                )
            }
        }

    override fun searchProductsWithReviews(query: String): Flow<List<ProductWithReviews>> =
        getProductsWithReviews().map { list ->
            list.filter { it.product.productName.contains(query, ignoreCase = true) }
        }

    override suspend fun insertProducts(products: List<ProductEntity>) {
        productsStateFlow.update { oldValues ->
            (products + oldValues).distinctBy(ProductEntity::productId)
        }
    }

    override suspend fun insertReviews(reviews: List<ReviewEntity>) {
        reviewsStateFlow.update { oldValues ->
            val combined = oldValues.toMutableList()
            reviews.forEach { newReview ->
                val existingIndex = combined.indexOfFirst {
                    it.productId == newReview.productId &&
                            it.name == newReview.name &&
                            it.text == newReview.text
                }
                if (existingIndex >= 0) {
                    combined[existingIndex] = newReview
                } else {
                    combined.add(newReview)
                }
            }
            combined
        }
    }

    override suspend fun deleteAllProducts() {
        productsStateFlow.update { emptyList() }
    }
}