package com.example.subscriptionmanager.common.data.network.error

/**
 * HTTP 통신 중 발생하는 커스텀 예외 클래스
 */
data class ApiException(
    val code: Int, // HTTP 상태 코드 (e.g., 401, 500)
    override val message: String? = null,
    val errorBody: String? = null
) : Exception(message)

/**
 * UI 레이어로 전달될 에러의 종류를 정의하는 Sealed Class
 */
sealed class NetworkError : Exception() { // NetworkError 자체도 Exception을 상속받도록 정의 가능

    // [수정] Unauthorized는 Exception을 상속
    data class Unauthorized(val msg: String = "인증 실패 (401)") : NetworkError()

    // [수정] ServerError는 Exception을 상속
    data class ServerError(val msg: String = "서버 오류 발생") : NetworkError()

    // [수정] UnknownError는 Exception을 상속
    data class UnknownError(val msg: String = "알 수 없는 네트워크 오류") : NetworkError()

    // 예시: toString()을 오버라이드하여 로그에 메시지가 잘 나오도록 할 수 있습니다.
    override val message: String
        get() = when (this) {
            is Unauthorized -> this.msg
            is ServerError -> this.msg
            is UnknownError -> this.msg
        }
}

/**
 * ApiException을 Domain Layer에서 사용할 NetworkError로 매핑하는 확장 함수
 */
fun ApiException.toNetworkError(): NetworkError {
    return when (this.code) {
        401 -> NetworkError.Unauthorized(this.message ?: "인증 토큰이 만료되었거나 유효하지 않습니다.")
        in 500..599 -> NetworkError.ServerError(this.message ?: "서버에 문제가 발생했습니다.")
        else -> NetworkError.UnknownError(this.message ?: "네트워크 요청 중 오류가 발생했습니다.")
    }
}

