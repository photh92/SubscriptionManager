package com.example.subscriptionmanager.subscription.domain.repository

import com.example.subscriptionmanager.subscription.domain.model.Subscription
import kotlinx.coroutines.flow.Flow

/**
 * Repository의 추상화 인터페이스.
 * Domain Layer는 이 인터페이스만 바라봅니다.
 */
interface SubscriptionRepository {

    // 전체 구독 목록 조회 (로컬/원격 데이터를 통합하여 제공)
    fun getAllSubscriptions(): Flow<List<Subscription>>

    // 구독 추가
    suspend fun addSubscription(subscription: Subscription)

    // 구독 수정
    suspend fun updateSubscription(subscription: Subscription)

    // 구독 삭제
    suspend fun deleteSubscription(subscriptionId: String)

    // 로컬과 원격 데이터를 동기화하는 로직
    suspend fun refreshSubscriptions()
}