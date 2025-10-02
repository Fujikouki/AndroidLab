package com.example.kouki.fujisue.androidlab.ui.workmanager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.kouki.fujisue.androidlab.worker.SimpleWorker
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkManagerScreen() {
    val context = LocalContext.current
    val workManager = WorkManager.getInstance(context)
    var workId by remember { mutableStateOf<UUID?>(null) }

    val flow = remember(workId) {
        workId?.let { workManager.getWorkInfoByIdFlow(it) }
    }
    val workInfo by flow?.collectAsState(null) ?: remember { mutableStateOf(null) }


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
            Text(
                text = "WorkManagerは、即時実行、長時間実行、および遅延実行が可能な、保証付きの非同期タスクをスケジュールするためのライブラリです。下のボタンを押すと、5秒間待機する単純なバックグラウンドタスクが開始されます。",
                style = MaterialTheme.typography.bodyLarge
            )

            Button(
                onClick = {
                    val workRequest = OneTimeWorkRequestBuilder<SimpleWorker>().build()
                    workId = workRequest.id
                    workManager.enqueue(workRequest)
                },
                enabled = workInfo?.state != WorkInfo.State.RUNNING
            ) {
                Text("バックグラウンドタスクを開始")
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (workInfo?.state) {
                WorkInfo.State.ENQUEUED -> Text("状態: タスクはキューに追加されました")
                WorkInfo.State.RUNNING -> Text("状態: タスク実行中...")
                WorkInfo.State.SUCCEEDED -> Text("状態: タスクは正常に完了しました")
                WorkInfo.State.FAILED -> Text("状態: タスクは失敗しました")
                WorkInfo.State.BLOCKED -> Text("状態: タスクはブロックされています")
                WorkInfo.State.CANCELLED -> Text("状態: タスクはキャンセルされました")
                null -> Text("状態: タスクはまだ開始されていません")
            }
        }
    }
}
