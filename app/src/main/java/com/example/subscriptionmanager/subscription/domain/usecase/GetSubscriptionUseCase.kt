package com.example.subscriptionmanager.subscription.domain.usecase

import com.example.subscriptionmanager.subscription.domain.model.Subscription
import com.example.subscriptionmanager.subscription.domain.repository.SubscriptionRepository
import javax.inject.Inject

class GetSubscriptionUseCase @Inject constructor(
    private val repository: SubscriptionRepository
) {
    /**
     * 특정 ID를 가진 구독을 Repository에서 가져옵니다.
     */
    suspend operator fun invoke(id: String): Subscription? {
        return repository.getSubscriptionById(id)
    }
}