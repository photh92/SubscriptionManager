package com.example.subscriptionmanager.subscription.di

import com.example.subscriptionmanager.subscription.data.repository.SubscriptionRepositoryImpl
import com.example.subscriptionmanager.subscription.domain.repository.SubscriptionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Subscription 도메인 관련 의존성 주입 모듈.
 * Domain Interface와 Data Layer 구현체를 연결합니다.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class SubscriptionModule {

    // SubscriptionRepository 인터페이스와 SubscriptionRepositoryImpl 구현체를 연결
    @Binds
    @Singleton
    abstract fun bindSubscriptionRepository(
        repositoryImpl: SubscriptionRepositoryImpl
    ): SubscriptionRepository
}