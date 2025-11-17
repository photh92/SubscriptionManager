package com.example.subscriptionmanager.subscription.presentation.subscription_list

import com.example.subscriptionmanager.subscription.domain.model.Subscription

/**
 * Subscription 목록 화면의 UI 상태를 정의하는 data class.
 * 모든 UI 요소는 이 상태 객체에 의해 결정됩니다.
 */
data class SubscriptionListState(
    // 표시할 실제 데이터 목록
    val subscriptions: List<Subscription> = emptyList(),

    // 로딩 중인지 여부 (초기 로딩이나 새로고침 시)
    val isLoading: Boolean = false,

    // 에러 발생 시 메시지
    val error: String? = null,

    // 전체 구독 비용 합계 등 파생된 정보
    val totalCost: Double = 0.0,

    // 성공 메시지
    val successMessage: String? = null
)