package com.example.subscriptionmanager.common

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

/**
 * Long (Unix Timestamp)와 LocalDate 간의 변환 유틸리티.
 * Domain Model (LocalDate)과 Data Layer (Long)의 타입 불일치를 해결합니다.
 */
object LongToLocalDateConverter {
    fun toLong(date: LocalDate): Long {
        return date.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
    }

    fun toLocalDate(timestamp: Long): LocalDate {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.UTC).toLocalDate()
    }
}