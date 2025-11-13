package com.example.subscriptionmanager.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.subscriptionmanager.data.local.dao.subscription.SubscriptionDao
import com.example.subscriptionmanager.data.local.entity.subscription.SubscriptionEntity

/**
 * Room Database 인스턴스를 정의하는 추상 클래스.
 */
@Database(
    entities = [SubscriptionEntity::class], // 사용할 Entity 지정
    version = 1, // DB 버전
    exportSchema = false // 스키마 버전 기록 파일 생성 비활성화
)
abstract class AppDatabase : RoomDatabase() {

    // DB에서 DAO를 가져오기 위한 추상 메서드
    abstract fun subscriptionDao(): SubscriptionDao
}