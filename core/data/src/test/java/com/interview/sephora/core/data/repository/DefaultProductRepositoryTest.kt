package com.interview.sephora.core.data.repository

import com.interview.sephora.core.data.model.asEntities
import com.interview.sephora.core.data.model.asEntity
import com.interview.sephora.core.data.testdoubles.TestProductDao
import com.interview.sephora.core.data.testdoubles.TestSephoraNetworkDataSource
import com.interview.sephora.core.testing.fake.sampleNetworkProducts
import com.interview.sephora.core.testing.fake.sampleNetworkReviews
import com.interview.sephora.core.testing.fake.sampleReplacementProduct
import com.interview.sephora.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DefaultProductRepositoryTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var productDao: TestProductDao
    private lateinit var networkDataSource: TestSephoraNetworkDataSource
    private lateinit var subject: DefaultProductRepository

    @Before
    fun setup() {
        productDao = TestProductDao()
        networkDataSource = TestSephoraNetworkDataSource()
        subject = DefaultProductRepository(
            productDao = productDao,
            networkDataSource = networkDataSource,
            ioDispatcher = mainDispatcherRule.testDispatcher,
        )
    }

    @Test
    fun `getProductsWithReviews emits empty list before sync`() = runTest {
        val result = subject.getProductsWithReviews().first()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getProductsWithReviews emits mapped products after sync`() = runTest {
        networkDataSource.setProducts(sampleNetworkProducts)
        networkDataSource.setReviews(emptyList())

        subject.sync()

        val result = subject.getProductsWithReviews().first()
        assertEquals(sampleNetworkProducts.size, result.size)
    }

    @Test
    fun `getProductsWithReviews maps network product to domain model correctly`() = runTest {
        networkDataSource.setProducts(listOf(sampleNetworkProducts.first()))
        networkDataSource.setReviews(emptyList())

        subject.sync()

        val product = subject.getProductsWithReviews().first().first()
        val expected = sampleNetworkProducts.first().asEntity()
        assertEquals(expected.productId, product.productId)
        assertEquals(expected.productName, product.productName)
        assertEquals(expected.description, product.description)
        assertEquals(expected.price, product.price)
        assertEquals(expected.brandId, product.brandId)
        assertEquals(expected.brandName, product.brandName)
        assertEquals(expected.isProductSet, product.isProductSet)
        assertEquals(expected.isSpecialBrand, product.isSpecialBrand)
    }

    @Test
    fun `getProductsWithReviews computes averageRating from synced reviews`() = runTest {
        networkDataSource.setProducts(listOf(sampleNetworkProducts.first()))
        networkDataSource.setReviews(listOf(sampleNetworkReviews.first()))

        subject.sync()

        val product = subject.getProductsWithReviews().first().first()
        assertEquals(3.0, product.averageRating)
    }

    @Test
    fun `getProductsWithReviews averageRating is null when product has no reviews`() = runTest {
        networkDataSource.setProducts(listOf(sampleNetworkProducts.first()))
        networkDataSource.setReviews(emptyList())

        subject.sync()

        val product = subject.getProductsWithReviews().first().first()
        assertNull(product.averageRating)
    }

    @Test
    fun `getProductsWithReviews maps reviews to domain model correctly`() = runTest {
        networkDataSource.setProducts(listOf(sampleNetworkProducts.first()))
        networkDataSource.setReviews(listOf(sampleNetworkReviews.first()))

        subject.sync()

        val reviews = subject.getProductsWithReviews().first().first().reviews
        val expectedEntities = sampleNetworkReviews.first().asEntities()
        assertEquals(expectedEntities.size, reviews.size)
        assertEquals(expectedEntities.first().name, reviews.first().name)
        assertEquals(expectedEntities.first().text, reviews.first().text)
        assertEquals(expectedEntities.first().rating, reviews.first().rating)
    }

    @Test
    fun `sync persists network products to dao`() = runTest {
        networkDataSource.setProducts(sampleNetworkProducts)
        networkDataSource.setReviews(emptyList())

        subject.sync()

        val result = subject.getProductsWithReviews().first()
        assertEquals(sampleNetworkProducts.size, result.size)
    }

    @Test
    fun `sync persists network reviews to dao`() = runTest {
        networkDataSource.setProducts(listOf(sampleNetworkProducts.first()))
        networkDataSource.setReviews(listOf(sampleNetworkReviews.first()))

        subject.sync()

        val reviews = subject.getProductsWithReviews().first().first().reviews
        assertEquals(sampleNetworkReviews.first().reviews?.size, reviews.size)
    }

    @Test
    fun `sync replaces existing products on subsequent call`() = runTest {
        networkDataSource.setProducts(sampleNetworkProducts)
        networkDataSource.setReviews(emptyList())
        subject.sync()

        networkDataSource.setProducts(listOf(sampleReplacementProduct))
        networkDataSource.setReviews(emptyList())
        subject.sync()

        val result = subject.getProductsWithReviews().first()
        assertEquals(1, result.size)
        assertEquals("New Product", result.first().productName)
    }

    // endregion

    // region searchProductsWithReviews

    @Test
    fun `searchProductsWithReviews returns products matching query`() = runTest {
        networkDataSource.setProducts(sampleNetworkProducts)
        networkDataSource.setReviews(emptyList())
        subject.sync()

        val result = subject.searchProductsWithReviews("Serum").first()

        assertEquals(1, result.size)
        assertEquals("Rose Serum", result.first().productName)
    }

    @Test
    fun `searchProductsWithReviews is case insensitive`() = runTest {
        networkDataSource.setProducts(sampleNetworkProducts)
        networkDataSource.setReviews(emptyList())
        subject.sync()

        val result = subject.searchProductsWithReviews("serum").first()

        assertEquals(1, result.size)
    }

    @Test
    fun `searchProductsWithReviews returns all products for empty query`() = runTest {
        networkDataSource.setProducts(sampleNetworkProducts)
        networkDataSource.setReviews(emptyList())
        subject.sync()

        val result = subject.searchProductsWithReviews("").first()

        assertEquals(sampleNetworkProducts.size, result.size)
    }

    @Test
    fun `searchProductsWithReviews returns empty list when no match`() = runTest {
        networkDataSource.setProducts(sampleNetworkProducts)
        networkDataSource.setReviews(emptyList())
        subject.sync()

        val result = subject.searchProductsWithReviews("zzznomatch").first()

        assertTrue(result.isEmpty())
    }

}


