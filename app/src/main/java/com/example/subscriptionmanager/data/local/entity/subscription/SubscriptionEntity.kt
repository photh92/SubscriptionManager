package com.example.subscriptionmanager.data.local.entity.subscription

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room Database - subscriptions 테이블의 구조를 정의하는 Entity.
 * @param remoteId 실제 서버 ID 또는 Mock ID.
 * Room 엔티티는 이 ID를 사용하여 Local/Remote 동기화를 수행합니다.
 */
@Entity(tableName = "subscriptions")
data class SubscriptionEntity(
    // Room DB 내에서 사용되는 로컬 ID (자동 증가)
    @PrimaryKey(autoGenerate = true)
    val localId: Long = 0,

    // Remote ID (Mock API나 실제 서버 ID). Local/Remote Sync의 기준이 됨.
    val remoteId: String,

    val name: String,
    val cost: Double,
    val cycle: String,
    // Unix Timestamp (Long)를 사용하여 저장
    val firstBillingDate: Long,
    val currency: String,
    val isActive: Boolean
)