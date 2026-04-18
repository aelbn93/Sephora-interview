package com.interview.sephora.core.data.model

import com.interview.sephora.core.database.model.ProductEntity
import com.interview.sephora.core.database.model.ProductWithReviews
import com.interview.sephora.core.database.model.ReviewEntity
import com.interview.sephora.core.model.Product
import com.interview.sephora.core.model.Review
import com.interview.sephora.core.network.model.NetworkProduct
import com.interview.sephora.core.network.model.NetworkProductReview

fun ProductWithReviews.asExternalModel(): Product {
    val productReviews = reviews.map { it.asExternalModel() }
    return product.asExternalModel().copy(
        averageRating = productReviews
            .mapNotNull { it.rating }
            .takeIf { it.isNotEmpty() }
            ?.average(),
        reviews = productReviews,
    )
}

fun ProductEntity.asExternalModel() = Product(
    productId = productId,
    productName = productName,
    description = description,
    price = price,
    imageSmall = imageSmall,
    imageLarge = imageLarge,
    brandId = brandId,
    brandName = brandName,
    isProductSet = isProductSet,
    isSpecialBrand = isSpecialBrand,
    averageRating = null,
    reviews = emptyList(),
)

fun ReviewEntity.asExternalModel() = Review(
    name = name,
    text = text,
    rating = rating,
)

fun NetworkProduct.asEntity() = ProductEntity(
    productId = productId ?: 0L,
    productName = productName.orEmpty(),
    description = description.orEmpty(),
    price = price ?: 0.0,
    imageSmall = imagesUrl?.small.orEmpty(),
    imageLarge = imagesUrl?.large.orEmpty(),
    brandId = brand?.id.orEmpty(),
    brandName = brand?.name.orEmpty(),
    isProductSet = isProductSet ?: false,
    isSpecialBrand = isSpecialBrand ?: false,
)

fun NetworkProductReview.asEntities(): List<ReviewEntity> =
    reviews?.map { review ->
        ReviewEntity(
            productId = productId ?: 0L,
            name = review.name,
            text = review.text,
            rating = review.rating,
        )
    } ?: emptyList()