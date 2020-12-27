package com.clay.dagger2testingdemo.di

import android.content.Context
import androidx.room.Room
import com.clay.dagger2testingdemo.data.local.WordDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object TestAppModule {

    @Provides
    @InMemoryDb
    fun providesInMemoryDb(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(context, WordDatabase::class.java)
            .allowMainThreadQueries()
            .build()

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class InMemoryDb