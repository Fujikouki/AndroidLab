package com.example.kouki.fujisue.androidlab.ui.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.kouki.fujisue.androidlab.ui.theme.AndroidLabTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionsScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Permission Examples") },
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
                "実行時のパーミッションリクエストのサンプルです。\n権限が必要な場合は「権限を要求」ボタンを押してください。",
                style = MaterialTheme.typography.bodyLarge
            )

            // カメラ権限
            PermissionRequestRow(
                permission = Manifest.permission.CAMERA,
                permissionName = "カメラ"
            )

            // 位置情報権限
            PermissionRequestRow(
                permission = Manifest.permission.ACCESS_COARSE_LOCATION,
                permissionName = "位置情報"
            )

            // 通知権限 (Android 13以上)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                PermissionRequestRow(
                    permission = Manifest.permission.POST_NOTIFICATIONS,
                    permissionName = "通知"
                )
            } else {
                Text(
                    "通知権限はAndroid 13以降でリクエスト可能です。",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                text = "注意：一度明示的に拒否をされると、アプリ側からは申請できなくなります。",
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun PermissionRequestRow(permission: String, permissionName: String) {
    val context = LocalContext.current
    // 権限の現在の状態を保持するState
    var permissionStatus by remember { mutableStateOf(getPermissionStatus(context, permission)) }

    // パーミッションリクエストの結果を受け取るランチャー
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionStatus = if (isGranted) "許可されています" else "拒否されました"
    }

    // 設定画面などから戻ってきた際に、権限の状態を再チェックしてUIを更新する
    LaunchedEffect(permission) {
        permissionStatus = getPermissionStatus(context, permission)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = permissionName, style = MaterialTheme.typography.titleMedium)
                Text(text = "状態: $permissionStatus", style = MaterialTheme.typography.bodyMedium)
            }
            // 権限が許可されていない場合のみボタンを表示
            if (permissionStatus != "許可されています") {
                Button(onClick = { launcher.launch(permission) }) {
                    Text("権限を要求")
                }
            }
        }
    }
}

private fun getPermissionStatus(context: Context, permission: String): String {
    return when (ContextCompat.checkSelfPermission(context, permission)) {
        PackageManager.PERMISSION_GRANTED -> "許可されています"
        else -> "許可されていません"
    }
}

@Preview(showBackground = true)
@Composable
fun PermissionsScreenPreview() {
    AndroidLabTheme {
        PermissionsScreen()
    }
}
