package com.dehdarian.task.di

import com.dehdarian.task.api.ArticleService
import com.dehdarian.task.api.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ArticleModule {

    /**
     * provide retrofit object and create instance from that
     */
    @Provides
    @Singleton
    fun provideRetrofitInstance(): ArticleService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ArticleService::class.java)
}