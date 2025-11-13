package com.example.subscriptionmanager.di

import android.app.Application
import androidx.room.Room
import com.example.subscriptionmanager.subscription.data.local.AppDatabase
import com.example.subscriptionmanager.subscription.data.local.dao.SubscriptionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Room Database 및 DAO 객체를 Hilt에 제공하는 모듈.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase =
        Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "subscription_db" // DB 이름
        )
            // 실제 프로젝트에서는 migration 처리를 해야 하지만, 포트폴리오 초기에는 간단히 fallback 설정
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideSubscriptionDao(db: AppDatabase): SubscriptionDao = db.subscriptionDao()
}
