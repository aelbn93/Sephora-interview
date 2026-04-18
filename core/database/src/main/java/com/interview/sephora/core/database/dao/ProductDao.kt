package com.interview.sephora.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.interview.sephora.core.database.model.ProductEntity
import com.interview.sephora.core.database.model.ProductWithReviews
import com.interview.sephora.core.database.model.ReviewEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReviews(reviews: List<ReviewEntity>)

    @Transaction
    suspend fun insertProductsWithReviews(
        products: List<ProductEntity>,
        reviews: List<ReviewEntity>,
    ) {
        insertProducts(products)
        insertReviews(reviews)
    }

    @Transaction
    @Query("SELECT * FROM products")
    fun getProductsWithReviews(): Flow<List<ProductWithReviews>>

    @Transaction
    @Query("SELECT * FROM products WHERE productName LIKE '%' || :query || '%'")
    fun searchProductsWithReviews(query: String): Flow<List<ProductWithReviews>>

    @Query("DELETE FROM products")
    suspend fun deleteAllProducts()

}