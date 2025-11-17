package com.example.subscriptionmanager.subscription.domain.usecase

import com.example.subscriptionmanager.subscription.domain.model.Subscription
import com.example.subscriptionmanager.subscription.domain.repository.SubscriptionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 구독 목록을 가져오는 비즈니스 로직(UseCase).
 * Repository에서 데이터를 가져와 가공 없이 그대로 ViewModel로 전달합니다.
 */
class GetSubscriptionsUseCase @Inject constructor(
    private val repository: SubscriptionRepository
) {
    // Invoke 오퍼레이터: 클래스 인스턴스를 함수처럼 호출할 수 있게 해줍니다.
    operator fun invoke(): Flow<List<Subscription>> {
        return repository.getAllSubscriptions()
    }
}