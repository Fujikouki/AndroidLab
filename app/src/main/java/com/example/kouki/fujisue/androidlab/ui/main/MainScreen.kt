package com.example.kouki.fujisue.androidlab.ui.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kouki.fujisue.androidlab.ui.navigation.Route

/**
 * 各画面への遷移リストをセクションごとに管理するためのデータクラス
 * @param title セクションのタイトル
 * @param screens セクションに含まれる画面のルートと名前のペアのリスト
 */
data class ScreenSection(val title: String, val screens: List<Pair<Any, String>>)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen(navController: NavController) {
    // 各画面をセクションごとに分類
    val screenSections = listOf(
        ScreenSection(
            title = "UI基礎",
            screens = listOf(
                Route.TextScreen to "テキスト表示を試す画面",
                Route.ButtonScreen to "ボタンのインタラクションを試す画面",
                Route.ImageScreen to "画像の表示を試す画面",
                Route.InputScreen to "ユーザー入力を試す画面",
                Route.LayoutsScreen to "レイアウト方法を学ぶ画面",
                Route.ListScreen to "リスト表示を試す画面",
                Route.DialogScreen to "ダイアログ表示を試す画面",
                Route.OtherScreen to "その他のUIコンポーネントを試す画面",
                Route.StateScreen to "Composeの状態について学ぶ画面"
            )
        ),
        ScreenSection(
            title = "UI応用",
            screens = listOf(
                Route.AnimationScreen to "アニメーションを試す画面",
                Route.ThemingScreen to "テーマ設定を学ぶ画面",
                Route.TouchingScreen to "タッチ操作を学ぶ画面",
                Route.CollapsingToolbarScreen to "スクロールと連動するUIを学ぶ画面",
                Route.CanvasScreen to "カスタム描画とCanvasを学ぶ画面",
                Route.ReorderableListScreen to "ドラッグ＆ドロップで並べ替え可能なリストを学ぶ画面",
                Route.WebViewScreen to "WebViewを学ぶ画面"
            )
        ),
        ScreenSection(
            title = "Androidフレームワーク",
            screens = listOf(
                Route.LifecycleScreen to "ライフサイクルを学ぶ画面",
                Route.SideEffectScreen to "副作用について学ぶ画面",
                Route.PermissionsScreen to "実行時パーミッションの扱い方を学ぶ画面",
                Route.NotificationScreen to "通知を学ぶ画面",
                Route.ActivityResultScreen to "ActivityResultを学ぶ画面",
                Route.SavedInstanceStateScreen to "savedInstanceStateを学ぶ画面",
                Route.CameraScreen to "カメラ機能を学ぶ画面",
                Route.LocationScreen to "GPS（位置情報）を学ぶ画面",
                Route.WorkManagerScreen to "WorkManagerを学ぶ画面",
                Route.SensorScreen to "センサーについて学ぶ画面"
            )
        ),
        ScreenSection(
            title = "バックエンド & アーキテクチャ",
            screens = listOf(
                Route.NetworkingScreen to "ネットワークリクエストとデータ表示を学ぶ画面",
                Route.StorageScreen to "データ永続化を学ぶ画面"
            )
        )
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
        ) {
            item {
                Text(
                    text = "このアプリは、Android開発における様々なUIコンポーネントや機能を試すための学習用アプリです。各項目を選択して、動作を確認してみてください。",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }

            screenSections.forEach { section ->
                stickyHeader {
                    Text(
                        text = section.title,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
                items(section.screens) { (route, name) ->
                    Button(
                        onClick = { navController.navigate(route) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        Text(name)
                    }
                }
            }
        }
    }
}
