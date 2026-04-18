package com.interview.sephora.core.database.di

import android.content.Context
import androidx.room.Room
import com.interview.sephora.core.database.SephoraDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun providesSephoraDatabase(
        @ApplicationContext context: Context,
    ): SephoraDatabase = Room.databaseBuilder(
        context,
        SephoraDatabase::class.java,
        "sephora-database",
    ).build()
}
