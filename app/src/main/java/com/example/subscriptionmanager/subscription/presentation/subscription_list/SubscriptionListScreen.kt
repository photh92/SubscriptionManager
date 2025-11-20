package com.example.subscriptionmanager.subscription.presentation.subscription_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.subscriptionmanager.subscription.domain.model.Subscription
import java.time.LocalDate
import java.util.UUID

/**
 * 구독 목록을 표시하는 메인 화면 Composable.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionListScreen(
    // Hilt를 사용하여 ViewModel을 제공받음
    viewModel: SubscriptionListViewModel = hiltViewModel()
) {
    // ViewModel의 StateFlow를 Composable 상태로 관찰 (Recomposition 트리거)
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("My Subscriptions") }) },
        floatingActionButton = {
            // 구독 추가 FAB
            FloatingActionButton(onClick = {
                viewModel.showAddDialog() // ViewModel 함수 호출

                // 현재는 테스트용 더미 데이터 추가 함수 호출 (ViewModel에 함수 정의 필요)
//                viewModel.onAddDummySubscription()
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Subscription")
            }
        }
    ) { paddingValues ->

        // 상태에 따라 다이얼로그 표시
        if (state.isAddDialogVisible) {
            AddSubscriptionDialog(
                onDismiss = viewModel::dismissAddDialog,
                onConfirm = { name, cost, cycle ->
                    // [Day 9] 입력 데이터를 Domain Model로 변환
                    val newSubscription = Subscription(
                        id = UUID.randomUUID().toString(), // 임시 고유 ID 생성
                        name = name,
                        cost = cost,
                        cycle = cycle,
                        firstBillingDate = LocalDate.now(), // 현재 날짜로 설정
                        currency = "KRW",
                        isActive = true
                    )
                    // ViewModel의 Add 이벤트 핸들러 호출
                    viewModel.onAddSubscription(newSubscription)
                    viewModel.dismissAddDialog() // 다이얼로그 닫기
                }
            )
        }

        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {

            // 상단 총합계 표시
            SubscriptionSummary(state.totalCost)

            // 로딩 상태 표시
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            // 에러 상태 표시
            else if (state.error != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: ${state.error}", color = MaterialTheme.colorScheme.error)
                }
            }
            // 데이터 목록 표시
            else {
                SubscriptionList(
                    subscriptions = state.subscriptions,
                    onItemClick = viewModel::onSubscriptionClicked // 이벤트 핸들러
                )
            }
        }
    }
}

@Composable
fun SubscriptionSummary(totalCost: Double) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Total Monthly Cost",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(4.dp))
            // 통화 형식으로 포맷팅하는 유틸리티 필요 (현재는 단순 표시)
            Text(
                text = String.format("￦ %.2f", totalCost),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun SubscriptionList(
    subscriptions: List<Subscription>,
    onItemClick: (String) -> Unit
) {
    if (subscriptions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No subscriptions added yet.", style = MaterialTheme.typography.bodyLarge)
        }
        return
    }

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(subscriptions, key = { it.id }) { subscription ->
            SubscriptionListItem(
                subscription = subscription,
                onClick = { onItemClick(subscription.id) }
            )
        }
    }
}

@Composable
fun SubscriptionListItem(
    subscription: Subscription,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 구독 이름
            Text(
                text = subscription.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            // 구독 비용 및 주기
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = String.format("￦ %.0f / %s", subscription.cost, subscription.cycle),
                    style = MaterialTheme.typography.bodyMedium
                )
                // 다음 결제일 (Domain Model의 계산된 값 사용)
                Text(
                    text = "Next: ${subscription.nextBillingDate}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Composable
fun AddSubscriptionDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, cost: Double, cycle: String) -> Unit // 입력 인자 확장
) {
    var nameInput by remember { mutableStateOf("") }
    var costInput by remember { mutableStateOf("") }
    var cycleInput by remember { mutableStateOf("MONTHLY") } // 기본값

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Subscription") },
        text = {
            Column {
                OutlinedTextField( // 이름 입력 필드
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    label = { Text("Subscription Name") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField( // 비용 입력 필드
                    value = costInput,
                    onValueChange = { costInput = it.filter { c -> c.isDigit() || c == '.' } }, // 숫자만 허용
                    label = { Text("Cost") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                // 간단한 주기를 선택하는 드롭다운 (현재는 Text로 대체)
                Text("Cycle: $cycleInput", modifier = Modifier.clickable {
                    cycleInput = if (cycleInput == "MONTHLY") "YEARLY" else "MONTHLY"
                })
            }
        },
        confirmButton = {
            // 유효성 검사 (이름과 비용이 비어있지 않고 비용이 Double로 변환 가능한지)
            val costDouble = costInput.toDoubleOrNull()
            val isValid = nameInput.isNotBlank() && costDouble != null

            Button(
                onClick = { onConfirm(nameInput, costDouble!!, cycleInput) },
                enabled = isValid
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text("Cancel") }
        }
    )
}