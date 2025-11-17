package com.example.subscriptionmanager.subscription.domain.usecase

import com.example.subscriptionmanager.subscription.domain.model.Subscription
import com.example.subscriptionmanager.subscription.domain.repository.SubscriptionRepository
import javax.inject.Inject

/**
 * 기존 구독 정보를 수정하는 UseCase.
 */
class UpdateSubscriptionUseCase @Inject constructor(
    private val repository: SubscriptionRepository
) {
    /**
     * @param subscription 수정된 내용을 담은 Subscription Domain Model 객체
     */
    suspend operator fun invoke(subscription: Subscription) {
        repository.updateSubscription(subscription)
    }
}