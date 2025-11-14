package com.example.subscriptionmanager.subscription.data.repository.mapper

import com.example.subscriptionmanager.common.LongToLocalDateConverter
import com.example.subscriptionmanager.subscription.data.local.entity.SubscriptionEntity
import com.example.subscriptionmanager.subscription.data.remote.dto.SubscriptionDto
import com.example.subscriptionmanager.subscription.domain.model.Subscription


/**
 * DTO, Entity, Domain Model 간의 변환을 담당하는 Mapper.
 */
fun SubscriptionDto.toEntity(localId: Long = 0): SubscriptionEntity {
    // DTO의 ID(서버 ID)를 Entity의 remoteId로 사용합니다.
    return SubscriptionEntity(
        localId = localId,
        remoteId = this.id ?: throw IllegalStateException("DTO ID must not be null for Entity conversion"),
        name = this.name,
        cost = this.cost,
        cycle = this.cycle,
        // DTO의 Long 타입 firstBillingDate를 그대로 사용
        firstBillingDate = this.firstBillingDate,
        currency = this.currency,
        isActive = this.isActive
    )
}

fun SubscriptionEntity.toDomain(): Subscription {
    return Subscription(
        id = this.remoteId, // Entity의 remoteId를 Domain Model의 ID로 사용
        name = this.name,
        cost = this.cost,
        cycle = this.cycle,
        firstBillingDate = LongToLocalDateConverter.toLocalDate(this.firstBillingDate),
        currency = this.currency,
        isActive = this.isActive
    )
}

fun SubscriptionDto.toDomain(): Subscription {
    return Subscription(
        id = this.id ?: throw IllegalStateException("Dto ID cannot be null when converting to Domain"),
        name = this.name,
        cost = this.cost,
        cycle = this.cycle,
        firstBillingDate = LongToLocalDateConverter.toLocalDate(this.firstBillingDate),
        currency = this.currency,
        isActive = this.isActive
    )
}

fun Subscription.toEntity(): SubscriptionEntity {
    return SubscriptionEntity(
        // localId는 Room에 의해 자동 생성되므로 0으로 둠 (또는 default 값을 그대로 둠)
        localId = 0,
        remoteId = this.id,
        name = this.name,
        cost = this.cost,
        cycle = this.cycle,
        firstBillingDate = LongToLocalDateConverter.toLong(this.firstBillingDate),
        currency = this.currency,
        isActive = this.isActive
    )
}

fun Subscription.toDto(): SubscriptionDto {
    return SubscriptionDto(
        id = this.id, // DTO는 ID를 포함하여 서버에 전송 (수정/삭제 시)
        name = this.name,
        cost = this.cost,
        cycle = this.cycle,
        firstBillingDate = LongToLocalDateConverter.toLong(this.firstBillingDate),
        currency = this.currency,
        isActive = this.isActive
    )
}