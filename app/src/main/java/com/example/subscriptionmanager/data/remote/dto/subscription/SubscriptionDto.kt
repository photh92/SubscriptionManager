package com.example.subscriptionmanager.data.remote.dto.subscription

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 서버와 통신하는 단일 구독 항목의 DTO (Data Transfer Object).
 * 이는 서버의 응답 또는 클라이언트의 요청 본문으로 사용됩니다.
 */
@JsonClass(generateAdapter = true)
data class SubscriptionDto(
    // 서버에서 할당하는 ID. (새 항목 추가 시에는 null일 수 있음)
    @field:Json(name = "id") val id: String? = null,

    @field:Json(name = "name") val name: String,

    // 결제 금액
    @field:Json(name = "cost") val cost: Double,

    // 결제 주기 (예: "Monthly", "Yearly")
    @field:Json(name = "billing_cycle") val cycle: String,

    // 첫 결제일 (다음 결제일 계산의 기준이 됨)
    @field:Json(name = "first_billing_date") val firstBillingDate: Long, // 서버 통신 시 Long(Unix Timestamp)을 선호

    // 통화 단위 (예: "KRW", "USD")
    @field:Json(name = "currency") val currency: String = "KRW",

    // 구독 활성화 여부
    @field:Json(name = "is_active") val isActive: Boolean = true
)