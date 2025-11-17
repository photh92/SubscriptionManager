package com.example.subscriptionmanager.subscription.domain.usecase

import com.example.subscriptionmanager.subscription.domain.model.Subscription
import com.example.subscriptionmanager.subscription.domain.repository.SubscriptionRepository
import javax.inject.Inject

/**
 * 새로운 구독을 추가하는 UseCase.
 */
class AddSubscriptionUseCase @Inject constructor(
    private val repository: SubscriptionRepository
) {
    /**
     * @param subscription 추가할 Subscription Domain Model 객체
     */
    suspend operator fun invoke(subscription: Subscription) {
        repository.addSubscription(subscription)
    }
}