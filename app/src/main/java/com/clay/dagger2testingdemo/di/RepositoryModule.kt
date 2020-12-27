package com.clay.dagger2testingdemo.di

import com.clay.dagger2testingdemo.repositories.DefaultWordRepository
import com.clay.dagger2testingdemo.repositories.WordRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun providesRepository(
        defaultWordRepository: DefaultWordRepository
    ): WordRepository

}