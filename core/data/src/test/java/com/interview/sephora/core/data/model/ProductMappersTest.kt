package com.interview.sephora.core.data.model

import com.interview.sephora.core.database.model.ReviewEntity
import com.interview.sephora.core.network.model.NetworkProduct
import com.interview.sephora.core.network.model.NetworkProductReview
import com.interview.sephora.core.network.model.NetworkReview
import com.interview.sephora.core.testing.fake.sampleNetworkProduct
import com.interview.sephora.core.testing.fake.sampleNetworkProductReview
import com.interview.sephora.core.testing.fake.sampleProductEntity
import com.interview.sephora.core.testing.fake.sampleProductWithReviews
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ProductMappersTest {


    @Test
    fun `productWithReviews asExternalModel maps product fields correctly`() {
        val result = sampleProductWithReviews.asExternalModel()

        assertEquals(1L, result.productId)
        assertEquals("Rose Serum", result.productName)
        assertEquals("A hydrating serum", result.description)
        assertEquals(45.99, result.price)
        assertEquals("https://example.com/small.jpg", result.imageSmall)
        assertEquals("https://example.com/large.jpg", result.imageLarge)
        assertEquals("brand_001", result.brandId)
        assertEquals("L'Oréal", result.brandName)
        assertEquals(false, result.isProductSet)
        assertEquals(false, result.isSpecialBrand)
    }

    @Test
    fun `productWithReviews asExternalModel computes averageRating from reviews`() {
        val result = sampleProductWithReviews.asExternalModel()

        assertEquals(3.0, result.averageRating)
    }

    @Test
    fun `productWithReviews asExternalModel averageRating is null when reviews are empty`() {
        val result = sampleProductWithReviews.copy(reviews = emptyList()).asExternalModel()

        assertNull(result.averageRating)
    }

    @Test
    fun `productWithReviews asExternalModel averageRating is null when all ratings are null`() {
        val result = sampleProductWithReviews.copy(
            reviews = listOf(
                ReviewEntity(productId = 1L, name = "Alice", text = "Ok", rating = null),
            )
        ).asExternalModel()

        assertNull(result.averageRating)
    }

    @Test
    fun `productWithReviews asExternalModel averageRating ignores null ratings in mixed list`() {
        val result = sampleProductWithReviews.copy(
            reviews = listOf(
                ReviewEntity(productId = 1L, name = "Alice", text = "Great", rating = 4.0),
                ReviewEntity(productId = 1L, name = "Bob", text = "No rating", rating = null),
            )
        ).asExternalModel()

        // only 4.0 counts → average = 4.0
        assertEquals(4.0, result.averageRating)
    }

    @Test
    fun `productWithReviews asExternalModel maps reviews list correctly`() {
        val result = sampleProductWithReviews.asExternalModel()

        assertEquals(2, result.reviews.size)
        assertEquals("Alice", result.reviews[0].name)
        assertEquals("Great!", result.reviews[0].text)
        assertEquals(4.0, result.reviews[0].rating)
        assertEquals("Bob", result.reviews[1].name)
        assertEquals("Decent", result.reviews[1].text)
        assertEquals(2.0, result.reviews[1].rating)
    }

    // endregion

    // region ProductEntity.asExternalModel()

    @Test
    fun `productEntity asExternalModel maps all fields correctly`() {
        val result = sampleProductEntity.asExternalModel()

        assertEquals(1L, result.productId)
        assertEquals("Rose Serum", result.productName)
        assertEquals("A hydrating serum", result.description)
        assertEquals(45.99, result.price)
        assertEquals("https://example.com/small.jpg", result.imageSmall)
        assertEquals("https://example.com/large.jpg", result.imageLarge)
        assertEquals("brand_001", result.brandId)
        assertEquals("L'Oréal", result.brandName)
        assertEquals(false, result.isProductSet)
        assertEquals(false, result.isSpecialBrand)
    }

    @Test
    fun `productEntity asExternalModel always has null averageRating and empty reviews`() {
        val result = sampleProductEntity.asExternalModel()

        assertNull(result.averageRating)
        assertTrue(result.reviews.isEmpty())
    }

    // endregion

    // region ReviewEntity.asExternalModel()

    @Test
    fun `reviewEntity asExternalModel maps all fields correctly`() {
        val entity =
            ReviewEntity(id = 1L, productId = 1L, name = "Alice", text = "Great!", rating = 4.0)

        val result = entity.asExternalModel()

        assertEquals("Alice", result.name)
        assertEquals("Great!", result.text)
        assertEquals(4.0, result.rating)
    }

    @Test
    fun `reviewEntity asExternalModel maps null fields correctly`() {
        val entity = ReviewEntity(id = 1L, productId = 1L, name = null, text = null, rating = null)

        val result = entity.asExternalModel()

        assertNull(result.name)
        assertNull(result.text)
        assertNull(result.rating)
    }

    // endregion

    // region NetworkProduct.asEntity()

    @Test
    fun `networkProduct asEntity maps all fields correctly`() {
        val result = sampleNetworkProduct.asEntity()

        assertEquals(1L, result.productId)
        assertEquals("Rose Serum", result.productName)
        assertEquals("A hydrating serum", result.description)
        assertEquals(45.99, result.price)
        assertEquals("https://example.com/small.jpg", result.imageSmall)
        assertEquals("https://example.com/large.jpg", result.imageLarge)
        assertEquals("brand_001", result.brandId)
        assertEquals("L'Oréal", result.brandName)
        assertEquals(false, result.isProductSet)
        assertEquals(false, result.isSpecialBrand)
    }

    @Test
    fun `networkProduct asEntity uses defaults when all fields are null`() {
        val result = NetworkProduct().asEntity()

        assertEquals(0L, result.productId)
        assertEquals("", result.productName)
        assertEquals("", result.description)
        assertEquals(0.0, result.price)
        assertEquals("", result.imageSmall)
        assertEquals("", result.imageLarge)
        assertEquals("", result.brandId)
        assertEquals("", result.brandName)
        assertEquals(false, result.isProductSet)
        assertEquals(false, result.isSpecialBrand)
    }

    @Test
    fun `networkProduct asEntity uses empty string when imagesUrl is null`() {
        val result = sampleNetworkProduct.copy(imagesUrl = null).asEntity()

        assertEquals("", result.imageSmall)
        assertEquals("", result.imageLarge)
    }

    @Test
    fun `networkProduct asEntity uses empty string when brand is null`() {
        val result = sampleNetworkProduct.copy(brand = null).asEntity()

        assertEquals("", result.brandId)
        assertEquals("", result.brandName)
    }

    // endregion

    // region NetworkProductReview.asEntities()

    @Test
    fun `networkProductReview asEntities maps all fields correctly`() {
        val result = sampleNetworkProductReview.asEntities()

        assertEquals(2, result.size)
        assertEquals(1L, result[0].productId)
        assertEquals("Alice", result[0].name)
        assertEquals("Great!", result[0].text)
        assertEquals(4.0, result[0].rating)
        assertEquals(1L, result[1].productId)
        assertEquals("Bob", result[1].name)
        assertEquals("Decent", result[1].text)
        assertEquals(2.0, result[1].rating)
    }

    @Test
    fun `networkProductReview asEntities returns empty list when reviews are null`() {
        val result = NetworkProductReview(productId = 1L, reviews = null).asEntities()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `networkProductReview asEntities uses 0 as productId when null`() {
        val result = NetworkProductReview(
            productId = null,
            reviews = listOf(NetworkReview(name = "Alice", text = "Nice", rating = 5.0))
        ).asEntities()

        assertEquals(0L, result.first().productId)
    }

    @Test
    fun `networkProductReview asEntities maps null review fields correctly`() {
        val result = NetworkProductReview(
            productId = 1L,
            reviews = listOf(NetworkReview(name = null, text = null, rating = null))
        ).asEntities()

        assertEquals(1, result.size)
        assertNull(result.first().name)
        assertNull(result.first().text)
        assertNull(result.first().rating)
    }

}

