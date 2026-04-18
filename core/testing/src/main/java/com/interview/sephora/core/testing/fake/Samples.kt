package com.interview.sephora.core.testing.fake

import com.interview.sephora.core.database.model.ProductEntity
import com.interview.sephora.core.database.model.ProductWithReviews
import com.interview.sephora.core.database.model.ReviewEntity
import com.interview.sephora.core.model.Product
import com.interview.sephora.core.network.model.NetworkBrand
import com.interview.sephora.core.network.model.NetworkImagesUrl
import com.interview.sephora.core.network.model.NetworkProduct
import com.interview.sephora.core.network.model.NetworkProductReview
import com.interview.sephora.core.network.model.NetworkReview


val sampleProducts = listOf(
    Product(
        productId = 1L,
        productName = "Rose Serum",
        description = "A hydrating rose serum",
        price = 45.99,
        imageSmall = "https://example.com/rose_small.jpg",
        imageLarge = "https://example.com/rose_large.jpg",
        brandId = "brand_001",
        brandName = "L'Oréal",
        isProductSet = false,
        isSpecialBrand = false,
        averageRating = 4.5,
        reviews = emptyList(),
    ),
    Product(
        productId = 2L,
        productName = "Blue Mask",
        description = "A deep cleansing blue mask",
        price = 29.99,
        imageSmall = "https://example.com/mask_small.jpg",
        imageLarge = "https://example.com/mask_large.jpg",
        brandId = "brand_002",
        brandName = "Clinique",
        isProductSet = false,
        isSpecialBrand = true,
        averageRating = 3.8,
        reviews = emptyList(),
    ),
    Product(
        productId = 3L,
        productName = "Night Cream",
        description = "A nourishing overnight cream",
        price = 59.99,
        imageSmall = "https://example.com/cream_small.jpg",
        imageLarge = "https://example.com/cream_large.jpg",
        brandId = "brand_003",
        brandName = "Estée Lauder",
        isProductSet = true,
        isSpecialBrand = false,
        averageRating = null,
        reviews = emptyList(),
    ),
)



val sampleProductEntity = ProductEntity(
    productId = 1L,
    productName = "Rose Serum",
    description = "A hydrating serum",
    price = 45.99,
    imageSmall = "https://example.com/small.jpg",
    imageLarge = "https://example.com/large.jpg",
    brandId = "brand_001",
    brandName = "L'Oréal",
    isProductSet = false,
    isSpecialBrand = false,
)

val sampleProductWithReviews = ProductWithReviews(
    product = sampleProductEntity,
    reviews = listOf(
        ReviewEntity(id = 1L, productId = 1L, name = "Alice", text = "Great!", rating = 4.0),
        ReviewEntity(id = 2L, productId = 1L, name = "Bob", text = "Decent", rating = 2.0),
    ),
)

val sampleNetworkProduct = NetworkProduct(
    productId = 1L,
    productName = "Rose Serum",
    description = "A hydrating serum",
    price = 45.99,
    imagesUrl = NetworkImagesUrl(
        small = "https://example.com/small.jpg",
        large = "https://example.com/large.jpg",
    ),
    brand = NetworkBrand(id = "brand_001", name = "L'Oréal"),
    isProductSet = false,
    isSpecialBrand = false,
)

val sampleNetworkProductReview = NetworkProductReview(
    productId = 1L,
    hide = false,
    reviews = listOf(
        NetworkReview(name = "Alice", text = "Great!", rating = 4.0),
        NetworkReview(name = "Bob", text = "Decent", rating = 2.0),
    ),
)


val sampleNetworkProducts = listOf(
    NetworkProduct(
        productId = 1L,
        productName = "Rose Serum",
        description = "A hydrating serum",
        price = 45.99,
        imagesUrl = NetworkImagesUrl(
            small = "https://example.com/small.jpg",
            large = "https://example.com/large.jpg",
        ),
        brand = NetworkBrand(id = "brand_001", name = "L'Oréal"),
        isProductSet = false,
        isSpecialBrand = false,
    ),
    NetworkProduct(
        productId = 2L,
        productName = "Blue Mask",
        description = "A deep cleansing mask",
        price = 29.99,
        imagesUrl = NetworkImagesUrl(
            small = "https://example.com/mask_small.jpg",
            large = "https://example.com/mask_large.jpg",
        ),
        brand = NetworkBrand(id = "brand_002", name = "Clinique"),
        isProductSet = false,
        isSpecialBrand = true,
    ),
)

val sampleNetworkReviews = listOf(
    NetworkProductReview(
        productId = 1L,
        hide = false,
        reviews = listOf(
            NetworkReview(name = "Alice", text = "Great!", rating = 4.0),
            NetworkReview(name = "Bob", text = "Decent", rating = 2.0),
        ),
    ),
)

val sampleReplacementProduct = NetworkProduct(
    productId = 99L,
    productName = "New Product",
    description = "A new product",
    price = 9.99,
    imagesUrl = null,
    brand = null,
    isProductSet = false,
    isSpecialBrand = false,
)