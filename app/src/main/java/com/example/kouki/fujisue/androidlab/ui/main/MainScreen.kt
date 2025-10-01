package com.example.kouki.fujisue.androidlab.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kouki.fujisue.androidlab.ui.navigation.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    val screens = listOf(
        Route.TextScreen to "テキスト表示を試す画面",
        Route.ButtonScreen to "ボタンのインタラクションを試す画面",
        Route.ImageScreen to "画像の表示を試す画面",
        Route.ListScreen to "リスト表示を試す画面",
        Route.DialogScreen to "ダイアログ表示を試す画面",
        Route.InputScreen to "ユーザー入力を試す画面",
        Route.LayoutsScreen to "レイアウト方法を学ぶ画面",
        Route.PermissionsScreen to "実行時パーミッションの扱い方を学ぶ画面",
        Route.NotificationScreen to "通知を学ぶ画面",
        Route.AnimationScreen to "アニメーションを試す画面",
        Route.ThemingScreen to "テーマ設定を学ぶ画面",
        Route.OtherScreen to "その他のUIコンポーネントを試す画面",
        Route.TouchingScreen to "タッチ操作を学ぶ画面",
        Route.StateManagementScreen to "状態管理を試す画面",
        Route.NetworkingScreen to "ネットワークリクエストとデータ表示を学ぶ画面",
        Route.StorageScreen to "データ永続化を学ぶ画面"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Android Lab - メインメニュー") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "このアプリは、Android開発における様々なUIコンポーネントや機能を試すための学習用アプリです。各項目を選択して、動作を確認してみてください。",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            items(screens) { (route, name) ->
                Button(
                    onClick = { navController.navigate(route) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(name)
                }
            }
        }
    }
}
