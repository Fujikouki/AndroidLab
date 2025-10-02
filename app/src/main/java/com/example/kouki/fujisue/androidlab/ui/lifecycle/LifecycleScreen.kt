package com.example.kouki.fujisue.androidlab.ui.lifecycle

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LifecycleScreen(
    viewModel: LifecycleViewModel = viewModel()
) {
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    // ViewModelからライフサイクルイベントのリストを購読
    val lifecycleEvents by viewModel.lifecycleEvents.collectAsState()

    val listState = rememberLazyListState()

    // DisposableEffectを使ってライフサイクルイベントを監視します
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            viewModel.addEvent(event)
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // 新しいイベントが追加されたときにリストの最下部に自動スクロールします
    LaunchedEffect(lifecycleEvents.size) {
        if (lifecycleEvents.isNotEmpty()) {
            listState.animateScrollToItem(index = lifecycleEvents.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lifecycle Observer with ViewModel") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "ViewModelを使ってライフサイクルイベントを監視しています。",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "以下の操作を試して、ログに記録されるイベントを確認してみましょう。",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "• ホームボタンを押して、再度アプリを開く\n" +
                                "• 他の画面に移動して、この画面に戻る\n" +
                                "• 端末を回転させる (状態が保持されることを確認)",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Lifecycle Event Log", style = MaterialTheme.typography.titleMedium)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                items(lifecycleEvents) { log ->
                    val color = when (log.event) {
                        Lifecycle.Event.ON_CREATE,
                        Lifecycle.Event.ON_START,
                        Lifecycle.Event.ON_RESUME -> Color.Blue

                        Lifecycle.Event.ON_PAUSE,
                        Lifecycle.Event.ON_STOP -> Color.Gray

                        Lifecycle.Event.ON_DESTROY -> Color.Red
                        else -> MaterialTheme.colorScheme.onSurface
                    }
                    Text(
                        text = "${log.timestamp}: ${log.event.name}",
                        color = color,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}
