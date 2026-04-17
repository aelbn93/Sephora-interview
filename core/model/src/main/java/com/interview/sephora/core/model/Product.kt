package com.interview.sephora.core.model

data class Product(
    val productId: Long,
    val productName: String,
    val description: String,
    val price: Double,
    val imageSmall: String,
    val imageLarge: String,
    val brandId: String,
    val brandName: String,
    val isProductSet: Boolean,
    val isSpecialBrand: Boolean,
    val averageRating: Double?,
    val reviews: List<Review>,
)