package com.example.kouki.fujisue.androidlab.ui.webview

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

private const val WEBVIEW_URL = "https://developer.android.com/jetpack/compose"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewScreen() {
    val url = WEBVIEW_URL

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("WebViewの表示") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ) { paddingValues ->
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            factory = {
                // AndroidViewのファクトリ内で、従来のWebViewインスタンスを作成
                WebView(it).apply {
                    // リンクをWebView内で開くための設定
                    webViewClient = WebViewClient()
                    // JavaScriptを無効化する
                    // 有効化して変化を確かめよう!!
                    settings.javaScriptEnabled = false
                    // URLを読み込む
                    loadUrl(url)
                }
            }
        )
    }
}
