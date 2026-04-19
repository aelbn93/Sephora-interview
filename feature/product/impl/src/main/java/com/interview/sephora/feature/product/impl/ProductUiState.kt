package com.interview.sephora.feature.product.impl

import com.interview.sephora.core.model.Product

sealed interface ProductUiState {
    data object Loading : ProductUiState
    data object Error : ProductUiState
    data class Success(val products: List<Product>) : ProductUiState
}