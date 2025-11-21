package com.example.subscriptionmanager.common.data.network.interceptor

import com.example.subscriptionmanager.common.data.network.error.ApiException
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import javax.inject.Inject

/**
 * HTTP 응답 코드를 검사하여 2xx가 아닌 경우 ApiException을 던지는 인터셉터
 */
class ErrorInterceptor @Inject constructor() : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        // HTTP 상태 코드가 200 범위(성공)가 아니면 예외 처리
        if (!response.isSuccessful) {
            val code = response.code
            val message = response.message

            // 응답 본문을 읽어와 errorBody에 저장 (log나 에러 상세 정보에 유용)
            val errorBodyString = response.body?.source()?.use { source ->
                source.request(Long.MAX_VALUE)
                Buffer().apply { writeAll(source) }.readUtf8()
            }

            // ApiException을 던져 Retrofit 호출 체인을 끊음
            throw ApiException(code, message, errorBodyString)
        }

        return response
    }
}