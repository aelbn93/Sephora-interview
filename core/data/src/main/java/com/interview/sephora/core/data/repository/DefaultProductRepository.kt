package com.interview.sephora.core.data.repository

import com.interview.sephora.common.network.Dispatcher
import com.interview.sephora.common.network.SephoraDispatchers.IO
import com.interview.sephora.core.data.model.asEntities
import com.interview.sephora.core.data.model.asEntity
import com.interview.sephora.core.data.model.asExternalModel
import com.interview.sephora.core.database.dao.ProductDao
import com.interview.sephora.core.model.Product
import com.interview.sephora.core.network.SephoraNetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultProductRepository @Inject constructor(
    private val productDao: ProductDao,
    private val networkDataSource: SephoraNetworkDataSource,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
) : ProductRepository {
    override fun getProductsWithReviews(): Flow<List<Product>> =
        productDao.getProductsWithReviews()
            .map { list -> list.map { it.asExternalModel() } }

    override suspend fun sync() = withContext(ioDispatcher) {
        coroutineScope {
            val productsDeferred = async { networkDataSource.fetchProducts() }
            val reviewsDeferred = async { networkDataSource.fetchProductReviews() }

            val networkProducts = productsDeferred.await()
            val networkReviews = reviewsDeferred.await()

            productDao.deleteAllProducts()
            productDao.insertProductsWithReviews(
                products = networkProducts.map { it.asEntity() },
                reviews = networkReviews.flatMap { it.asEntities() },
            )
        }
    }

    override fun searchProductsWithReviews(query: String): Flow<List<Product>> =
        productDao.searchProductsWithReviews(query)
            .map { list -> list.map { it.asExternalModel() } }

}