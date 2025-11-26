package com.example.subscriptionmanager.subscription.presentation.add_edit_subscription

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditSubscriptionScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddEditSubscriptionViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // 저장 성공 시 네비게이션 백 처리
    LaunchedEffect(key1 = state.isSaved) {
        if (state.isSaved) {
            onNavigateBack()
        }
    }

    // 에러 발생 시 스낵바 표시
    LaunchedEffect(key1 = state.error) {
        state.error?.let { message ->
            snackbarHostState.showSnackbar(message)
            // Note: 실제 앱에서는 에러 메시지를 ViewModel에서 null로 리셋하는 로직이 필요합니다.
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(if (state.name.isBlank()) "새 구독 추가" else "구독 편집: ${state.name}") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // 저장 버튼
                    IconButton(
                        onClick = { viewModel.onEvent(AddEditSubscriptionEvent.SaveSubscription) },
                        enabled = !state.isLoading // 로딩 중에는 저장 비활성화
                    ) {
                        Icon(Icons.Filled.Done, contentDescription = "Save")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. 이름 입력 필드
            OutlinedTextField(
                value = state.name,
                onValueChange = { viewModel.onEvent(AddEditSubscriptionEvent.EnteredName(it)) },
                label = { Text("구독 이름") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // 2. 비용 입력 필드
            OutlinedTextField(
                value = state.cost,
                onValueChange = {
                    // 숫자와 소수점만 허용
                    val filtered = it.filter { c -> c.isDigit() || c == '.' }
                    viewModel.onEvent(AddEditSubscriptionEvent.EnteredCost(filtered))
                },
                label = { Text("월별/연간 비용 (KRW)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // 3. 주기 선택
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CycleButton(
                    cycle = "MONTHLY",
                    isSelected = state.cycle == "MONTHLY",
                    onClick = { viewModel.onEvent(AddEditSubscriptionEvent.SelectedCycle("MONTHLY")) }
                )
                CycleButton(
                    cycle = "YEARLY",
                    isSelected = state.cycle == "YEARLY",
                    onClick = { viewModel.onEvent(AddEditSubscriptionEvent.SelectedCycle("YEARLY")) }
                )
            }

            // 4. 첫 결제일 표시 (날짜 선택기는 생략하고 텍스트만 표시)
            Text(
                text = "첫 결제일: ${state.firstBillingDate.format(DateTimeFormatter.ISO_DATE)}",
                style = MaterialTheme.typography.bodyMedium
            )

            // 로딩 인디케이터 표시 (편집 모드에서 데이터 로드 시)
            if (state.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
fun RowScope.CycleButton(cycle: String, isSelected: Boolean, onClick: () -> Unit) {
    AssistChip(
        onClick = onClick,
        label = { Text(cycle) },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
            labelColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = Modifier.weight(1f)
    )
}