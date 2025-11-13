package com.example.subscriptionmanager.data.remote.api.subscription

import com.example.subscriptionmanager.data.remote.dto.subscription.SubscriptionDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * 서버와의 구독 정보를 통신하기 위한 Retrofit API 인터페이스 (Mock Service로 대체 예정)
 */
interface SubscriptionApi {

    /**
     * 구독 목록을 조회합니다.
     */
    @GET("subscriptions")
    suspend fun getSubscriptionList(): SubscriptionListResponse

    /**
     * 새로운 구독을 추가합니다.
     */
    @POST("subscriptions")
    suspend fun addSubscription(@Body subscriptionDto: SubscriptionDto): SubscriptionDto

    /**
     * 기존 구독 정보를 수정합니다.
     */
    @PUT("subscriptions/{id}")
    suspend fun updateSubscription(@Path("id") id: String, @Body subscriptionDto: SubscriptionDto): SubscriptionDto

    /**
     * 특정 구독을 삭제합니다.
     */
    @DELETE("subscriptions/{id}")
    suspend fun deleteSubscription(@Path("id") id: String): Unit
}