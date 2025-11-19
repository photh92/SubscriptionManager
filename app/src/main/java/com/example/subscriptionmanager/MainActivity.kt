package com.example.subscriptionmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.subscriptionmanager.subscription.presentation.subscription_list.SubscriptionListScreen
import com.example.subscriptionmanager.ui.theme.SubscriptionManagerTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Hilt를 사용하기 위해 AndroidEntryPoint를 추가하고,
 * 최상위 Composable인 SubscriptionListScreen을 연결합니다.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SubscriptionManagerTheme {
                // 앱의 기본 배경
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 메인 화면 Composable 호출
                    SubscriptionListScreen()
                }
            }
        }
    }
}