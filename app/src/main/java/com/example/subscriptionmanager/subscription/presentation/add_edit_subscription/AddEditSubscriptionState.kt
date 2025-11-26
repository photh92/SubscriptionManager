package com.example.subscriptionmanager.subscription.presentation.add_edit_subscription

import java.time.LocalDate

/**
 * Add/Edit 화면의 현재 UI 상태를 정의합니다.
 */
data class AddEditSubscriptionState(
    val name: String = "",
    val cost: String = "", // Double 대신 String으로 받아 유효성 검사
    val cycle: String = "MONTHLY",
    val firstBillingDate: LocalDate = LocalDate.now(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSaved: Boolean = false
)

/**
 * Add/Edit 화면에서 발생하는 사용자 이벤트를 정의합니다.
 */
sealed class AddEditSubscriptionEvent {
    data class EnteredName(val value: String) : AddEditSubscriptionEvent()
    data class EnteredCost(val value: String) : AddEditSubscriptionEvent()
    data class SelectedCycle(val cycle: String) : AddEditSubscriptionEvent()
    // [추가] 날짜 선택 이벤트가 필요할 경우를 대비하여 추가
    data class SelectedFirstBillingDate(val date: LocalDate) : AddEditSubscriptionEvent()
    object SaveSubscription : AddEditSubscriptionEvent()
}