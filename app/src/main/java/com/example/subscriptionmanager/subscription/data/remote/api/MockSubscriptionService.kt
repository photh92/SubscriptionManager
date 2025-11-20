package com.example.subscriptionmanager.subscription.data.remote.api

import IdGenerator
import com.example.subscriptionmanager.subscription.data.remote.dto.SubscriptionDto
import kotlinx.coroutines.delay
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

/**
 * 실제 백엔드 API가 없을 때 사용하는 Mock 구현체.
 * SubscriptionApi 인터페이스를 구현하며, 메모리 내에서 데이터를 관리합니다.
 * ID는 타임스탬프 기반으로 생성되며, CRUD 기능이 구현되어 있습니다.
 */
class MockSubscriptionService : SubscriptionApi {

    // Unix Timestamp (Long)와 LocalDate 간의 변환 유틸리티
    private fun LocalDate.toEpochMilli(): Long =
        this.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

    private fun Long.toLocalDate(): LocalDate =
        Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDate()

    // Mock 데이터 저장소 (휘발성)
    private val mockSubscriptions = mutableListOf(
        SubscriptionDto(
            id = IdGenerator.generateUniqueId(),
            name = "넷플릭스",
            cost = 13500.0,
            cycle = "Monthly",
            firstBillingDate = LocalDate.of(2023, 3, 29).toEpochMilli(),
            currency = "KRW",
            isActive = true
        ),
        SubscriptionDto(
            id = IdGenerator.generateUniqueId(),
            name = "스포티파이",
            cost = 10900.0,
            cycle = "Monthly",
            firstBillingDate = LocalDate.of(2024, 11, 10).toEpochMilli(),
            currency = "KRW",
            isActive = true
        ),
        SubscriptionDto(
            id = IdGenerator.generateUniqueId(),
            name = "어도비 CC",
            cost = 62000.0,
            cycle = "Yearly",
            firstBillingDate = LocalDate.of(2025, 10, 1).toEpochMilli(),
            currency = "KRW",
            isActive = true
        )
    )

    // 네트워크 지연을 흉내냅니다.
    private suspend fun mockDelay() = delay(500)

    /**
     * 구독 목록 조회 (Read)
     */
    override suspend fun getAllSubscriptions(): List<SubscriptionDto> {
        delay(500) // 네트워크 지연 효과
        return mockSubscriptions.toList()
    }

    /**
     * 새로운 구독 추가 (Create)
     */
    override suspend fun addSubscription(subscriptionDto: SubscriptionDto): SubscriptionDto {
        mockDelay()
        // ID가 null인 경우에만 새로 생성 (클라이언트가 ID를 모르는 경우)
        val newId = subscriptionDto.id ?: IdGenerator.generateUniqueId()

        val newSubscription = subscriptionDto.copy(id = newId)

        // 중복 ID 방지 (Mocking 안정성 확보)
        if (mockSubscriptions.any { it.id == newId }) {
            throw IllegalArgumentException("Subscription with ID $newId already exists.")
        }

        mockSubscriptions.add(newSubscription)
        return newSubscription
    }

    /**
     * 기존 구독 정보 수정 (Update)
     */
    override suspend fun updateSubscription(id: String, subscriptionDto: SubscriptionDto): SubscriptionDto {
        mockDelay()
        val index = mockSubscriptions.indexOfFirst { it.id == id }

        return if (index != -1) {
            // ID는 URL Path에서 가져온 것을 사용하고, 나머지 정보만 업데이트
            val updatedSubscription = subscriptionDto.copy(id = id)
            mockSubscriptions[index] = updatedSubscription
            updatedSubscription
        } else {
            // 업데이트할 대상을 찾지 못한 경우 예외 발생
            throw NoSuchElementException("Subscription with ID $id not found for update.")
        }
    }

    /**
     * 특정 구독 삭제 (Delete)
     */
    override suspend fun deleteSubscription(id: String) {
        mockDelay()
        val removed = mockSubscriptions.removeIf { it.id == id }
        if (!removed) {
            // 삭제할 대상을 찾지 못한 경우 예외 발생
            throw NoSuchElementException("Subscription with ID $id not found for deletion.")
        }
    }
}