package com.example.subscriptionmanager.subscription.data.repository

import com.example.subscriptionmanager.subscription.data.local.dao.SubscriptionDao
import com.example.subscriptionmanager.subscription.data.remote.api.SubscriptionApi
import com.example.subscriptionmanager.subscription.domain.model.Subscription
import com.example.subscriptionmanager.subscription.domain.repository.SubscriptionRepository
import com.example.subscriptionmanager.subscription.data.repository.mapper.toDomain
import com.example.subscriptionmanager.subscription.data.repository.mapper.toDto
import com.example.subscriptionmanager.subscription.data.repository.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Data Layer의 Repository 구현체.
 * Local DB (Dao)와 Remote API를 통합하여 Domain Model을 제공합니다.
 */
class SubscriptionRepositoryImpl @Inject constructor(
    private val localDao: SubscriptionDao,
    private val remoteApi: SubscriptionApi
) : SubscriptionRepository {

    // 로컬 DB Flow를 Domain Model Flow로 매핑하여 반환
    override fun getAllSubscriptions(): Flow<List<Subscription>> {
        return localDao.getAllSubscriptions()
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }

    override suspend fun addSubscription(subscription: Subscription) {
        // 1. [로컬 저장 - 임시 ID 사용]
        // Domain Model이 가진 ID (임시 ID 또는 null)를 사용하여 Entity로 변환하고 로컬 DB에 저장합니다.
        localDao.insert(subscription.toEntity())

        try {
            // 2. [API 통신 및 서버 ID 수신]
            // DTO를 서버로 전송합니다. 서버는 이 데이터를 DB에 저장하고,
            // 최종적으로 부여된 서버 ID를 포함한 DTO를 응답으로 돌려줍니다.
            val remoteDto = remoteApi.addSubscription(subscription.toDto())

            // 3. [로컬 업데이트 - 최종 서버 ID로 갱신]
            remoteDto.id?.let { finalServerId ->
                // 서버에서 받은 최종 ID로 로컬 Entity를 업데이트합니다.
                // (Mapper를 통해 DTO -> Entity로 변환하여 업데이트)
                val updatedEntity = remoteDto.toEntity().copy(remoteId = finalServerId)
                localDao.update(updatedEntity)

                // Note: 이 로직이 작동하려면 update 로직이 remoteId를 기준으로 엔티티를 찾아서
                // localId가 아닌 remoteId를 기준으로 업데이트 할 수 있어야 합니다.
            }
        } catch (e: Exception) {
            // 4. [오류 처리]
            // API 통신 실패 시: 로컬에 저장된 항목을 '전송 실패' 또는 '대기 중' 상태로 표시하고,
            // 나중에 재시도 로직(Day 9의 Refresh/Sync 로직)이 처리하도록 남겨둡니다.
            // 현재는 간단히 에러를 던지거나 로깅합니다.
            // localDao.markAsPendingSync(subscription.id)
            throw e
        }
    }

    override suspend fun updateSubscription(subscription: Subscription) {
        // 로컬 DB 업데이트
        localDao.update(subscription.toEntity())

        // Mock API 수정 요청
        remoteApi.updateSubscription(subscription.id, subscription.toDto())
    }

    override suspend fun deleteSubscription(subscriptionId: String) {
        // 로컬 DB 삭제
        localDao.deleteByRemoteId(subscriptionId)

        // Mock API 삭제 요청
        remoteApi.deleteSubscription(subscriptionId)
    }

    // Mock API에서 데이터를 가져와 로컬 DB를 덮어쓰는 동기화 로직
    override suspend fun refreshSubscriptions() {
        // 현재는 비어있음. 구현 예정
    }
}