package com.example.kouki.fujisue.androidlab.ui.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.kouki.fujisue.androidlab.R
import com.example.kouki.fujisue.androidlab.ui.theme.AndroidLabTheme

private const val CHANNEL_ID = "android_lab_channel"
private const val NOTIFICATION_ID = 1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen() {
    val context = LocalContext.current
    var hasNotificationPermission by remember {
        mutableStateOf(hasNotificationPermission(context))
    }
    var notificationText by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableIntStateOf(NotificationManager.IMPORTANCE_DEFAULT) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasNotificationPermission = isGranted
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notification Example") },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("通知に表示したいメッセージを入力してください。")

            TextField(
                value = notificationText,
                onValueChange = { notificationText = it },
                label = { Text("通知メッセージ") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("通知の優先度 (Priority) を選択してください。")

            val priorityOptions = listOf(
                "なし" to NotificationCompat.PRIORITY_MIN,
                "低" to NotificationCompat.PRIORITY_LOW,
                "中" to NotificationCompat.PRIORITY_DEFAULT,
                "高" to NotificationCompat.PRIORITY_HIGH,
                "緊急" to NotificationCompat.PRIORITY_MAX
            )

            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                priorityOptions.forEach { (label, priority) ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = priorityOptions.indexOfFirst { it.second == priority },
                            count = priorityOptions.size
                        ),
                        onClick = { selectedPriority = priority },
                        selected = selectedPriority == priority
                    ) {
                        Text(label)
                    }
                }
            }
            Text(
                text = "注意: Android 8.0以降では、通知の優先度はチャンネルの重要度(Importance)によって決まります。",
                style = MaterialTheme.typography.bodySmall
            )

            Button(
                onClick = {
                    if (hasNotificationPermission) {
                        val message = notificationText.ifBlank { "入力がありませんでした。" }
                        showCustomNotification(context, message, selectedPriority)
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (hasNotificationPermission) "通知を送信" else "通知の権限を要求")
            }

            if (!hasNotificationPermission) {
                Text(
                    text = "Android 13以降では、通知を表示するために権限の許可が必要です。",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

private fun hasNotificationPermission(context: Context): Boolean {
    // Android 13 (Tiramisu) 未満は権限不要
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        return true
    }
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.POST_NOTIFICATIONS
    ) == PackageManager.PERMISSION_GRANTED
}

private fun showCustomNotification(context: Context, message: String, priority: Int) {
    createNotificationChannel(context)

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground) // 表示される小さなアイコン
        .setContentTitle("Android Labからの通知")
        .setContentText(message) // ユーザーが入力したメッセージ
        .setPriority(priority) // 選択された優先度
        .setAutoCancel(true) // ユーザーがタップしたら通知を自動的に削除

    // Android Studioの不具合で権限チェックの警告が出ることがあるが、
    // 呼び出し元でパーミッションチェック済みのため無視して問題ない
    with(NotificationManagerCompat.from(context)) {
        notify(NOTIFICATION_ID, builder.build())
    }
}

private fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Android Lab Channel"
        val descriptionText = "Android Labのテスト通知用チャンネルです。"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        // チャンネルをシステムに登録
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationScreenPreview() {
    AndroidLabTheme {
        NotificationScreen()
    }
}
