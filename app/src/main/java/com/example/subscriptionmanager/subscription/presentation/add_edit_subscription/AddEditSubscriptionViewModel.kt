package com.example.subscriptionmanager.subscription.presentation.add_edit_subscription

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.subscriptionmanager.subscription.domain.model.Subscription
import com.example.subscriptionmanager.subscription.domain.usecase.AddSubscriptionUseCase
import com.example.subscriptionmanager.subscription.domain.usecase.GetSubscriptionUseCase
import com.example.subscriptionmanager.subscription.domain.usecase.UpdateSubscriptionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

@HiltViewModel
class AddEditSubscriptionViewModel @Inject constructor(
    private val getSubscriptionUseCase: GetSubscriptionUseCase,
    private val addSubscriptionUseCase: AddSubscriptionUseCase,
    private val updateSubscriptionUseCase: UpdateSubscriptionUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(AddEditSubscriptionState())
    val state: StateFlow<AddEditSubscriptionState> = _state.asStateFlow()

    // 현재 편집 중인 구독의 ID (null이면 추가 모드)
    private var currentSubscriptionId: String? = null

    init {
        // 네비게이션 인자에서 ID를 추출
        savedStateHandle.get<String>("subscriptionId")?.let { id ->
            if (id != "new") {
                currentSubscriptionId = id
                loadSubscription(id) // ID가 있으면 편집 모드로 진입 및 데이터 로드
            }
        }
    }

    private fun loadSubscription(id: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                getSubscriptionUseCase(id)?.let { subscription ->
                    _state.update {
                        it.copy(
                            name = subscription.name,
                            cost = subscription.cost.toString(),
                            cycle = subscription.cycle,
                            firstBillingDate = subscription.firstBillingDate, // LocalDate 사용
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = "구독 로드 실패: ${e.message}") }
            }
        }
    }

    fun onEvent(event: AddEditSubscriptionEvent) {
        when (event) {
            is AddEditSubscriptionEvent.EnteredName -> _state.update { it.copy(name = event.value) }
            is AddEditSubscriptionEvent.EnteredCost -> _state.update { it.copy(cost = event.value) }
            is AddEditSubscriptionEvent.SelectedCycle -> _state.update { it.copy(cycle = event.cycle) }
            // 날짜 선택 이벤트 처리
            is AddEditSubscriptionEvent.SelectedFirstBillingDate -> _state.update { it.copy(firstBillingDate = event.date) }
            is AddEditSubscriptionEvent.SaveSubscription -> saveSubscription()
        }
    }

    private fun saveSubscription() {
        viewModelScope.launch {
            val current = _state.value
            val costDouble = current.cost.toDoubleOrNull()

            // 입력값 유효성 검사
            if (current.name.isBlank() || costDouble == null || costDouble <= 0) {
                _state.update { it.copy(error = "이름과 유효한 비용을 입력해주세요.") }
                return@launch
            }

            _state.update { it.copy(error = null) }

            try {
                // Domain Model 생성 시 firstBillingDate에 LocalDate 값을 전달
                val subscriptionToSave = Subscription(
                    id = currentSubscriptionId ?: UUID.randomUUID().toString(),
                    name = current.name,
                    cost = costDouble!!,
                    cycle = current.cycle,
                    firstBillingDate = current.firstBillingDate, // LocalDate 사용
                    currency = "KRW",
                    isActive = true
                )

                if (currentSubscriptionId != null) {
                    updateSubscriptionUseCase(subscriptionToSave)
                } else {
                    addSubscriptionUseCase(subscriptionToSave)
                }

                _state.update { it.copy(isSaved = true) }
            } catch (e: Exception) {
                _state.update { it.copy(error = "저장 중 오류 발생: ${e.message}") }
            }
        }
    }
}