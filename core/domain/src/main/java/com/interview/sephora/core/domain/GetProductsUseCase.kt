package com.interview.sephora.core.domain

import com.interview.sephora.core.data.repository.ProductRepository
import com.interview.sephora.core.model.Product
import com.interview.sephora.core.model.ReviewSortField
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository,
) {
    operator fun invoke(
        query: String = "",
        sortBy: ReviewSortField = ReviewSortField.BEST_TO_WORST,
    ): Flow<List<Product>> {
        val productsFlow = if (query.trim().isEmpty()) {
            productRepository.getProductsWithReviews()
        } else {
            productRepository.searchProductsWithReviews(query.trim())
        }

        return productsFlow.map { products ->
            products.map { product ->
                product.copy(
                    reviews = when (sortBy) {
                        ReviewSortField.BEST_TO_WORST -> product.reviews.sortedByDescending { it.rating }
                        ReviewSortField.WORST_TO_BEST -> product.reviews.sortedBy { it.rating }
                        ReviewSortField.NONE -> product.reviews
                    }
                )
            }
        }
    }
}