package com.interview.sephora.core.database


import com.interview.sephora.core.database.model.ProductEntity
import com.interview.sephora.core.database.model.ReviewEntity
import com.interview.sephora.core.database.DatabaseTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class ProductDaoTest : DatabaseTest() {


    @Test
    fun getProductsWithReviews_emptyWhenNothingInserted() = runTest {
        val result = productDao.getProductsWithReviews().first()

        assertTrue(result.isEmpty())
    }

    @Test
    fun getProductsWithReviews_returnsAllInsertedProducts() = runTest {
        insertProducts()

        val result = productDao.getProductsWithReviews().first()

        assertEquals(
            listOf(1L, 2L),
            result.map { it.product.productId },
        )
    }

    @Test
    fun getProductsWithReviews_returnsEmptyReviews_whenNoReviewsInserted() = runTest {
        insertProducts()

        val result = productDao.getProductsWithReviews().first()

        assertTrue(result.all { it.reviews.isEmpty() })
    }

    @Test
    fun getProductsWithReviews_mapsReviewsToCorrectProduct() = runTest {
        insertProducts()
        insertReviews()

        val result = productDao.getProductsWithReviews().first()
        val product1 = result.first { it.product.productId == 1L }
        val product2 = result.first { it.product.productId == 2L }

        assertEquals(2, product1.reviews.size)
        assertTrue(product1.reviews.all { it.productId == 1L })
        assertTrue(product2.reviews.isEmpty())
    }


    @Test
    fun insertProducts_replacesOnConflict() = runTest {
        insertProducts()
        productDao.insertProducts(
            listOf(testProductEntity(productId = 1L, productName = "Updated Serum")),
        )

        val result = productDao.getProductsWithReviews().first()

        assertEquals(2, result.size)
        assertEquals("Updated Serum", result.first { it.product.productId == 1L }.product.productName)
    }

    @Test
    fun insertReviews_associatesReviewsWithCorrectProduct() = runTest {
        insertProducts()
        insertReviews()

        val result = productDao.getProductsWithReviews().first()

        assertEquals(2, result.first { it.product.productId == 1L }.reviews.size)
    }

    @Test
    fun insertReviews_replacesOnConflict() = runTest {
        insertProducts()
        insertReviews()

        val updatedReviews = listOf(
            testReviewEntity(id = 1L, productId = 1L, name = "Alice", text = "Updated review"),
        )
        productDao.insertReviews(updatedReviews)

        val reviews = productDao.getProductsWithReviews().first()
            .first { it.product.productId == 1L }.reviews

        assertEquals("Updated review", reviews.first { it.id == 1L }.text)
    }


    @Test
    fun insertProductsWithReviews_insertsProductsAndReviewsInOneCall() = runTest {
        productDao.insertProductsWithReviews(
            products = testProductEntities,
            reviews = testReviewEntities,
        )

        val result = productDao.getProductsWithReviews().first()

        assertEquals(2, result.size)
        assertEquals(2, result.first { it.product.productId == 1L }.reviews.size)
    }

    @Test
    fun searchProductsWithReviews_returnsMatchingProducts() = runTest {
        insertProducts()

        val result = productDao.searchProductsWithReviews("Serum").first()

        assertEquals(1, result.size)
        assertEquals("Rose Serum", result.first().product.productName)
    }

    @Test
    fun searchProductsWithReviews_isCaseInsensitive() = runTest {
        insertProducts()

        val result = productDao.searchProductsWithReviews("serum").first()

        assertEquals(1, result.size)
    }

    @Test
    fun searchProductsWithReviews_returnsAllProducts_forEmptyQuery() = runTest {
        insertProducts()

        val result = productDao.searchProductsWithReviews("").first()

        assertEquals(2, result.size)
    }

    @Test
    fun searchProductsWithReviews_returnsEmpty_whenNoMatch() = runTest {
        insertProducts()

        val result = productDao.searchProductsWithReviews("zzznomatch").first()

        assertTrue(result.isEmpty())
    }

    @Test
    fun searchProductsWithReviews_includesReviews_inResults() = runTest {
        insertProducts()
        insertReviews()

        val result = productDao.searchProductsWithReviews("Serum").first()

        assertEquals(2, result.first().reviews.size)
    }


    @Test
    fun deleteAllProducts_removesAllProducts() = runTest {
        insertProducts()

        productDao.deleteAllProducts()

        val result = productDao.getProductsWithReviews().first()
        assertTrue(result.isEmpty())
    }

    @Test
    fun deleteAllProducts_cascadesDeleteToReviews() = runTest {
        insertProducts()
        insertReviews()
        productDao.deleteAllProducts()

        productDao.insertProducts(testProductEntities)
        val result = productDao.getProductsWithReviews().first()

        assertTrue(result.all { it.reviews.isEmpty() })
    }


    @Test
    fun deleteAllReviews_removesAllReviews_butKeepsProducts() = runTest {
        insertProducts()
        insertReviews()

        productDao.deleteAllReviews()

        val result = productDao.getProductsWithReviews().first()
        assertEquals(2, result.size)
        assertTrue(result.all { it.reviews.isEmpty() })
    }


    private suspend fun insertProducts() {
        productDao.insertProducts(testProductEntities)
    }

    private suspend fun insertReviews() {
        productDao.insertReviews(testReviewEntities)
    }
}


private val testProductEntities = listOf(
    testProductEntity(productId = 1L, productName = "Rose Serum"),
    testProductEntity(productId = 2L, productName = "Blue Mask"),
)

private val testReviewEntities = listOf(
    testReviewEntity(id = 1L, productId = 1L, name = "Alice", text = "Great!"),
    testReviewEntity(id = 2L, productId = 1L, name = "Bob", text = "Decent"),
)

private fun testProductEntity(
    productId: Long,
    productName: String,
) = ProductEntity(
    productId = productId,
    productName = productName,
    description = "",
    price = 0.0,
    imageSmall = "",
    imageLarge = "",
    brandId = "",
    brandName = "",
    isProductSet = false,
    isSpecialBrand = false,
)

private fun testReviewEntity(
    id: Long = 0L,
    productId: Long,
    name: String? = null,
    text: String? = null,
    rating: Double? = null,
) = ReviewEntity(
    id = id,
    productId = productId,
    name = name,
    text = text,
    rating = rating,
)