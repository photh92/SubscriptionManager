package com.example.subscriptionmanager.subscription.domain.usecase

import com.example.subscriptionmanager.subscription.domain.repository.SubscriptionRepository
import javax.inject.Inject

/**
 * 특정 구독을 삭제(취소)하는 UseCase.
 */
class DeleteSubscriptionUseCase @Inject constructor(
    private val repository: SubscriptionRepository
) {
    /**
     * @param subscriptionId 삭제할 구독의 고유 ID
     */
    suspend operator fun invoke(subscriptionId: String) {
        repository.deleteSubscription(subscriptionId)
    }
}