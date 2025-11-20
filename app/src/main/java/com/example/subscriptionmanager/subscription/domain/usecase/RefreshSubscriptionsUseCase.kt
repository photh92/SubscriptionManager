package com.example.subscriptionmanager.subscription.domain.usecase

import com.example.subscriptionmanager.subscription.domain.repository.SubscriptionRepository
import javax.inject.Inject

/**
 * 원격 데이터를 로컬에 동기화하는 UseCase.
 */
class RefreshSubscriptionsUseCase @Inject constructor(
    private val repository: SubscriptionRepository
) {
    suspend operator fun invoke() {
        repository.refreshSubscriptions()
    }
}