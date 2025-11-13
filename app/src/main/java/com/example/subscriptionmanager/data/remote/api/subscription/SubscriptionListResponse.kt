package com.example.subscriptionmanager.data.remote.api

import SubscriptionDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 구독 목록 조회 API의 최종 응답을 위한 DTO.
 * DTO 리스트와 메타데이터(예: 총 개수)를 포함합니다.
 */
@JsonClass(generateAdapter = true)
data class SubscriptionListResponse(

    // 구독 DTO 목록
    @field:Json(name = "subscriptions") val subscriptions: List<SubscriptionDto>,

    // 서버가 보유한 전체 구독 개수 (페이지네이션 등에 유용)
    @field:Json(name = "total_count") val totalCount: Int
)