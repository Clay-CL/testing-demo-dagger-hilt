package com.clay.dagger2testingdemo.di

import android.content.Context
import androidx.room.Room
import com.clay.dagger2testingdemo.data.local.WordDatabase
import com.clay.dagger2testingdemo.other.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun providesWordDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, WordDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun providesWordDao(wordDatabase: WordDatabase) = wordDatabase.wordDao()

}