package com.example.subscriptionmanager.subscription.presentation.subscription_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.subscriptionmanager.subscription.domain.model.Subscription

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
                onConfirm = { name, cost ->
                    // TODO: 입력받은 데이터를 Domain Model로 변환하여 viewModel.onAddSubscription() 호출
                    viewModel.dismissAddDialog()
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
    onConfirm: (String, Double) -> Unit // 임시로 이름과 비용만 받는다고 가정
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Subscription") },
        text = {
            // TODO: 입력 필드 (TextField, Dropdown) 구현
            Text("Subscription form goes here...")
        },
        confirmButton = {
            Button(onClick = {
                // 임시로 더미 데이터 전달 후 확인
                onConfirm("Dummy", 0.0)
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}