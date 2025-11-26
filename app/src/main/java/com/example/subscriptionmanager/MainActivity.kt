package com.example.subscriptionmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.subscriptionmanager.subscription.presentation.add_edit_subscription.AddEditSubscriptionScreen
import com.example.subscriptionmanager.subscription.presentation.subscription_list.SubscriptionListScreen
import com.example.subscriptionmanager.ui.theme.SubscriptionManagerTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * 네비게이션 경로 정의 (라우팅 맵)
 */
object Screen {
    const val SUBSCRIPTION_LIST = "subscription_list"

    // ID를 쿼리 파라미터로 받는 경로 템플릿: ?subscriptionId={subscriptionId}
    const val ADD_EDIT_SUBSCRIPTION = "add_edit_subscription?subscriptionId={subscriptionId}"

    const val SUBSCRIPTION_ID_KEY = "subscriptionId"

    // 경로 생성 함수 (네비게이션 호출 시 사용)
    fun createAddRoute() = "add_edit_subscription?subscriptionId=new"
    fun createEditRoute(id: String) = "add_edit_subscription?subscriptionId=$id"
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SubscriptionManagerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 앱의 최상위 Composable로 NavHost를 호출합니다.
                    SubscriptionApp()
                }
            }
        }
    }
}

/**
 * 전체 앱의 네비게이션 그래프를 정의하는 Composable
 */
@Composable
fun SubscriptionApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.SUBSCRIPTION_LIST // 앱 시작 시 첫 화면
    ) {
        // 1. 구독 목록 화면 정의
        composable(Screen.SUBSCRIPTION_LIST) {
            SubscriptionListScreen(
                // FAB 클릭 시: Add/Edit 화면으로 이동 (ID="new" 전달)
                onNavigateToAdd = {
                    navController.navigate(Screen.createAddRoute())
                },
                // 아이템 클릭 시: Add/Edit 화면으로 이동 (실제 ID 전달)
                onNavigateToEdit = { subscriptionId ->
                    navController.navigate(Screen.createEditRoute(subscriptionId))
                }
            )
        }

        // 2. 구독 추가/편집 화면 정의
        composable(
            route = Screen.ADD_EDIT_SUBSCRIPTION,
            arguments = listOf(
                navArgument(Screen.SUBSCRIPTION_ID_KEY) {
                    type = NavType.StringType
                    defaultValue = "new"
                }
            )
        ) {
            AddEditSubscriptionScreen(
                // 뒤로가기 버튼 클릭 시: 이전 화면으로 돌아가기
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}