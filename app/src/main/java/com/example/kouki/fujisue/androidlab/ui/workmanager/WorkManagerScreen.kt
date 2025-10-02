package com.example.kouki.fujisue.androidlab.ui.workmanager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.kouki.fujisue.androidlab.worker.NotificationWorker
import com.example.kouki.fujisue.androidlab.worker.SimpleWorker
import java.util.UUID
import java.util.concurrent.TimeUnit

const val PERIODIC_WORK_TAG = "periodic_notification_work"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkManagerScreen() {
    val context = LocalContext.current
    val workManager = WorkManager.getInstance(context)
    var oneTimeWorkId by remember { mutableStateOf<UUID?>(null) }

    // 1回限りのタスクの状態を監視
    val oneTimeWorkInfo by remember(oneTimeWorkId) {
        oneTimeWorkId?.let { workManager.getWorkInfoByIdFlow(it) }
    }?.collectAsState(initial = null) ?: remember { mutableStateOf(null) }

    // 定期実行タスクの状態を監視
    val periodicWorkInfo by workManager.getWorkInfosByTagFlow(PERIODIC_WORK_TAG)
        .collectAsState(initial = emptyList())

    val isPeriodicWorkScheduled =
        periodicWorkInfo.any { it.state == WorkInfo.State.ENQUEUED || it.state == WorkInfo.State.RUNNING }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("WorkManagerの学習") },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1回限りのタスク
            TaskCard(
                title = "1回限りのタスク",
                description = "5秒間待機する単純なバックグラウンドタスクです。",
                buttonText = "タスクを開始",
                buttonEnabled = oneTimeWorkInfo?.state != WorkInfo.State.RUNNING,
                status = when (oneTimeWorkInfo?.state) {
                    WorkInfo.State.ENQUEUED -> "状態: キューに追加されました"
                    WorkInfo.State.RUNNING -> "状態: 実行中..."
                    WorkInfo.State.SUCCEEDED -> "状態: 正常に完了しました"
                    WorkInfo.State.FAILED -> "状態: 失敗しました"
                    WorkInfo.State.BLOCKED -> "状態: ブロックされています"
                    WorkInfo.State.CANCELLED -> "状態: キャンセルされました"
                    else -> "状態: 開始されていません"
                },
                onButtonClick = {
                    val workRequest = OneTimeWorkRequestBuilder<SimpleWorker>().build()
                    oneTimeWorkId = workRequest.id
                    workManager.enqueue(workRequest)
                }
            )

            // 定期実行タスク
            TaskCard(
                title = "定期実行タスク",
                description = "15分ごとに通知を表示するタスクをスケジュールします。",
                buttonText = if (isPeriodicWorkScheduled) "タスクをキャンセル" else "タスクを開始",
                buttonEnabled = true,
                status = if (isPeriodicWorkScheduled) "状態: スケジュール済み" else "状態: 停止中",
                onButtonClick = {
                    if (isPeriodicWorkScheduled) {
                        workManager.cancelAllWorkByTag(PERIODIC_WORK_TAG)
                    } else {
                        val periodicWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
                            15, TimeUnit.MINUTES
                        ).addTag(PERIODIC_WORK_TAG).build()
                        workManager.enqueueUniquePeriodicWork(
                            PERIODIC_WORK_TAG,
                            ExistingPeriodicWorkPolicy.KEEP,
                            periodicWorkRequest
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun TaskCard(
    title: String,
    description: String,
    buttonText: String,
    buttonEnabled: Boolean,
    status: String,
    onButtonClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            Text(text = description, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = status, style = MaterialTheme.typography.bodySmall)
                Button(onClick = onButtonClick, enabled = buttonEnabled) {
                    Text(buttonText)
                }
            }
        }
    }
}
