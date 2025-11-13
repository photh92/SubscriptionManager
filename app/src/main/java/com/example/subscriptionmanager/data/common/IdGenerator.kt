/**
 * 고유 ID 생성 유틸리티.
 * 밀리초 타임스탬프와 랜덤 값을 조합하여 ID 중복 가능성을 극도로 낮춥니다.
 */
object IdGenerator {
    /**
     * s_[타임스탬프]_[랜덤값] 형식의 고유 ID를 생성합니다.
     */
    fun generateUniqueId(): String {
        // 현재 밀리초 타임스탬프
        val timestamp = System.currentTimeMillis()
        // 0부터 999 사이의 랜덤 값 (같은 밀리초에 여러 개 생성될 경우 대비)
        val randomPart = (0..999).random()
        return "s_${timestamp}_$randomPart"
    }
}