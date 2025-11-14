package com.example.subscriptionmanager.subscription.domain.model

import java.time.LocalDate

/**
 * Domain Layer의 핵심 비즈니스 모델.
 * UI와 UseCase에서 사용하며, 특정 인프라(Room, Retrofit)에 의존하지 않습니다.
 */
data class Subscription(
    val id: String, // Remote/Local을 아우르는 고유 식별자
    val name: String,
    val cost: Double,
    val cycle: String,
    val firstBillingDate: LocalDate,
    val currency: String,
    val isActive: Boolean,

    // Domain Model에만 존재하는 계산된 속성
    val nextBillingDate: LocalDate = calculateNextBillingDate(firstBillingDate, cycle)
)

private fun calculateNextBillingDate(firstDate: LocalDate, cycle: String): LocalDate {
    // cycle에 따라 1개월/1년 추가
    return when (cycle.uppercase()) {
        "MONTHLY" -> firstDate.plusMonths(1)
        "YEARLY" -> firstDate.plusYears(1)
        else -> firstDate
    }
}