package com.example.subscriptionmanager.common.data.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * 모든 HTTP 요청에 인증 헤더를 추가하는 인터셉터
 */
class AuthInterceptor @Inject constructor() : Interceptor {

    // 실제 앱에서는 SharedPreferences나 DataStore에서 토큰을 가져와야 함
    private val authToken = "Bearer mock_auth_token_12345"

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // 요청 헤더에 Authorization 추가
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", authToken)
            .build()

        return chain.proceed(newRequest)
    }
}