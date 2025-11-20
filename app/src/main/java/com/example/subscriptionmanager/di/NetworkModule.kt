package com.example.subscriptionmanager.di

import com.example.subscriptionmanager.subscription.data.remote.api.MockSubscriptionService
import com.example.subscriptionmanager.subscription.data.remote.api.SubscriptionApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

//    @Provides
//    @Singleton
//    fun provideOkHttpClient(): OkHttpClient =
//        OkHttpClient.Builder()
//            .addInterceptor(HttpLoggingInterceptor().apply {
//                level = HttpLoggingInterceptor.Level.BODY
//            })
//            .build()
//
//    @Provides
//    @Singleton
//    fun provideRetrofit(client: OkHttpClient): Retrofit =
//        Retrofit.Builder()
//            .baseUrl("https://api.example.com/")
//            .client(client)
//            .addConverterFactory(MoshiConverterFactory.create())
//            .build()
//
//    @Provides
//    @Singleton
//    fun provideApiService(retrofit: Retrofit): SubscriptionApi =
//        retrofit.create(SubscriptionApi::class.java)

    // MockAPI
    @Provides
    @Singleton
    // 이제 MockService를 SubscriptionApi 타입으로 제공
    fun provideSubscriptionApi(): SubscriptionApi {
        return MockSubscriptionService()
    }
}
