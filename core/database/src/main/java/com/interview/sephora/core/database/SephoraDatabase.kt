package com.interview.sephora.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.interview.sephora.core.database.dao.ProductDao
import com.interview.sephora.core.database.model.ProductEntity
import com.interview.sephora.core.database.model.ReviewEntity

@Database(
    entities = [
        ProductEntity::class,
        ReviewEntity::class
    ],
    version = 1,
    exportSchema = true,
)
internal abstract class SephoraDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
}
