package com.interview.sephora.core.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class ProductWithReviews(
    @Embedded val product: ProductEntity,
    @Relation(
        parentColumn = "productId",
        entityColumn = "productId",
    )
    val reviews: List<ReviewEntity>,
)