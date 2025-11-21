package com.example.subscriptionmanager.di

import com.example.subscriptionmanager.common.data.network.interceptor.AuthInterceptor
import com.example.subscriptionmanager.common.data.network.interceptor.ErrorInterceptor
import com.example.subscriptionmanager.subscription.data.remote.api.MockSubscriptionService
import com.example.subscriptionmanager.subscription.data.remote.api.SubscriptionApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

//    @Provides
//    @Singleton
//    fun provideOkHttpClient(
//        authInterceptor: AuthInterceptor, // Hilt 주입
//        errorInterceptor: ErrorInterceptor // Hilt 주입
//    ): OkHttpClient {
//        // 로깅 인터셉터는 개발 단계에서 가장 먼저 실행되도록 추가
//        val loggingInterceptor = HttpLoggingInterceptor().apply {
//            level = HttpLoggingInterceptor.Level.BODY
//        }
//
//        return OkHttpClient.Builder()
//            .addInterceptor(loggingInterceptor)
//            .addInterceptor(authInterceptor) // 인증 인터셉터
//            .addInterceptor(errorInterceptor) // 에러 검사 인터셉터
//            .connectTimeout(30, TimeUnit.SECONDS)
//            .readTimeout(30, TimeUnit.SECONDS)
//            .build()
//    }

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
