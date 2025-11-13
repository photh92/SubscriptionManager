package com.example.subscriptionmanager.data.local.dao.subscription

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.subscriptionmanager.data.local.entity.subscription.SubscriptionEntity
import kotlinx.coroutines.flow.Flow

/**
 * Room DB에 접근하여 CRUD 작업을 수행하는 인터페이스.
 */
@Dao
interface SubscriptionDao {

    // 구독 목록 전체 조회. Flow를 반환하여 데이터 변경 시 자동 업데이트 지원
    @Query("SELECT * FROM subscriptions")
    fun getAllSubscriptions(): Flow<List<SubscriptionEntity>>

    // 특정 구독 조회
    @Query("SELECT * FROM subscriptions WHERE remoteId = :remoteId")
    suspend fun getSubscriptionById(remoteId: String): SubscriptionEntity?

    // 구독 추가 및 업데이트. remoteId 충돌 시 덮어쓰기 (Sync 로직의 핵심)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(subscription: SubscriptionEntity): Long

    // 여러 개의 구독 목록 삽입 (대량 Sync 시 사용)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(subscriptions: List<SubscriptionEntity>)

    // 구독 정보 업데이트
    @Update
    suspend fun update(subscription: SubscriptionEntity)

    // 특정 구독 삭제 (remoteId 기준)
    @Query("DELETE FROM subscriptions WHERE remoteId = :remoteId")
    suspend fun deleteByRemoteId(remoteId: String)

    // 전체 데이터 삭제 (테스트 또는 초기화 시 사용)
    @Query("DELETE FROM subscriptions")
    suspend fun clearAll()
}