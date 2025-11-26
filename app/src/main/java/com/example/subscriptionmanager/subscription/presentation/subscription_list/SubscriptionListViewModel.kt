package com.example.subscriptionmanager.subscription.presentation.subscription_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.subscriptionmanager.subscription.domain.model.Subscription
import com.example.subscriptionmanager.subscription.domain.usecase.AddSubscriptionUseCase
import com.example.subscriptionmanager.subscription.domain.usecase.DeleteSubscriptionUseCase
import com.example.subscriptionmanager.subscription.domain.usecase.GetSubscriptionsUseCase
import com.example.subscriptionmanager.subscription.domain.usecase.RefreshSubscriptionsUseCase
import com.example.subscriptionmanager.subscription.domain.usecase.UpdateSubscriptionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 구독 목록 화면의 상태 및 이벤트를 처리하는 ViewModel.
 */
@HiltViewModel
class SubscriptionListViewModel @Inject constructor(
    private val getSubscriptionsUseCase: GetSubscriptionsUseCase,
    // 새로 추가된 UseCase들을 주입받음
    private val addSubscriptionUseCase: AddSubscriptionUseCase,
    private val updateSubscriptionUseCase: UpdateSubscriptionUseCase,
    private val deleteSubscriptionUseCase: DeleteSubscriptionUseCase,
    private val refreshSubscriptionsUseCase: RefreshSubscriptionsUseCase
) : ViewModel() {

    // [핵심] UI 상태를 외부로 노출하는 StateFlow
    private val _state = MutableStateFlow(SubscriptionListState())
    val state: StateFlow<SubscriptionListState> = _state.asStateFlow()

    init {
        // 앱 시작 시, 로컬 구독 목록 로드 전에 동기화 작업 시도
        viewModelScope.launch {
            try {
                refreshSubscriptionsUseCase() // 서버 데이터 동기화
            } catch (e: Exception) {
                // 동기화 실패는 에러로 간주하지 않고 로깅만 하고 로컬 데이터 로드를 계속함
                println("Initial sync failed: ${e.message}")
            } finally {
                loadSubscriptions() // 로드 함수는 이제 로컬 DB만 바라봄
            }
        }
    }

    /**
     * UI 테스트를 위한 더미 데이터 추가 이벤트 핸들러
     */
    fun onAddDummySubscription() {
        val dummySubscription = Subscription(
            id = System.currentTimeMillis().toString(), // 임시 ID
            name = "Test Subscription ${state.value.subscriptions.size + 1}",
            cost = 9.99 * (state.value.subscriptions.size + 1),
            cycle = if (state.value.subscriptions.size % 2 == 0) "MONTHLY" else "YEARLY",
            firstBillingDate = java.time.LocalDate.now().plusDays(5),
            currency = "KRW",
            isActive = true
        )

        // UseCase를 호출
        viewModelScope.launch {
            addSubscriptionUseCase(dummySubscription)
        }
    }

    private fun loadSubscriptions() {
        // 로딩 상태를 먼저 업데이트
        _state.update { it.copy(isLoading = true, error = null) }

        getSubscriptionsUseCase()
            // Flow의 데이터를 받아서 UI 상태로 변환 (Data Mapping)
            .map { subscriptions ->
                SubscriptionListState(
                    subscriptions = subscriptions,
                    isLoading = false,
                    // 전체 비용 계산 예시
                    totalCost = subscriptions.sumOf { it.cost }
                )
            }
            .catch { e ->
                // 에러 발생 시 UI 상태 업데이트
                _state.value = SubscriptionListState(
                    isLoading = false,
                    error = "데이터 로딩 중 오류 발생: ${e.message}"
                )
            }
            // 수집 결과를 _state에 반영
            .onEach { newState ->
                _state.value = newState
            }
            // ViewModel 생명주기에 맞춰 Flow 수집 시작
            .launchIn(viewModelScope)
    }

    /**
     * 구독 추가 이벤트 처리
     */
    fun onAddSubscription(subscription: Subscription) {
        viewModelScope.launch {
            try {
                // UseCase 호출
                addSubscriptionUseCase(subscription)
            } catch (e: Exception) {
                // 에러 처리 (UI 상태에 반영하거나 Toast 메시지 발생)
                _state.update { it.copy(error = "구독 추가 실패: ${e.message}") }
            }
        }
    }

    /**
     * 구독 수정 이벤트 처리
     */
    fun onUpdateSubscription(subscription: Subscription) {
        viewModelScope.launch {
            try {
                // UseCase 호출
                updateSubscriptionUseCase(subscription)
            } catch (e: Exception) {
                _state.update { it.copy(error = "구독 수정 실패: ${e.message}") }
            }
        }
    }

    /**
     * 구독 삭제 이벤트 처리
     */
    fun onDeleteSubscription(subscriptionId: String) {
        viewModelScope.launch {
            try {
                // UseCase 호출
                deleteSubscriptionUseCase(subscriptionId)
            } catch (e: Exception) {
                _state.update { it.copy(error = "구독 삭제 실패: ${e.message}") }
            }
        }
    }

    /**
     * 구독 추가 Dialog를 표시
     */
    fun showAddDialog() {
        _state.update { it.copy(isAddDialogVisible = true) }
    }

    /**
     * 구독 추가 Dialog를 숨김
     */
    fun dismissAddDialog() {
        _state.update { it.copy(isAddDialogVisible = false) }
    }
}