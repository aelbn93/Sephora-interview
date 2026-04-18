package com.interview.sephora.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
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
)