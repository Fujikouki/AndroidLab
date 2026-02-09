package com.example.kouki.fujisue.androidlab.ui.deeplink

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kouki.fujisue.androidlab.ui.theme.AndroidLabTheme

private const val CUSTOM_SCHEME_LINK = "androidlab://demo/deeplink"
private const val WEB_LINK = "https://androidlab.example.com/deeplink"

/**
 * ディープリンクの基本動作を学ぶための画面。
 * 指定URIをACTION_VIEWで起動し、この画面へ戻ってくる流れを確認できます。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeepLinkScreen() {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ディープリンクの学習") },
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "下のリンクを起動すると、Androidのディープリンク経由でこの画面が開きます。",
                style = MaterialTheme.typography.bodyLarge
            )

            DeepLinkCard(
                title = "Custom Scheme",
                uri = CUSTOM_SCHEME_LINK,
                buttonLabel = "androidlab:// を開く",
                onClick = {
                    openDeepLink(context = context, uri = CUSTOM_SCHEME_LINK)
                }
            )

            DeepLinkCard(
                title = "Web Link",
                uri = WEB_LINK,
                buttonLabel = "https:// を開く",
                onClick = {
                    openDeepLink(context = context, uri = WEB_LINK)
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "検証例: adb shell am start -a android.intent.action.VIEW -d \"$CUSTOM_SCHEME_LINK\"",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "検証例: adb shell am start -a android.intent.action.VIEW -d \"$WEB_LINK\"",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

/**
 * ディープリンクURIの情報表示と起動ボタンをまとめたカード。
 */
@Composable
private fun DeepLinkCard(
    title: String,
    uri: String,
    buttonLabel: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(text = uri, style = MaterialTheme.typography.bodyMedium)
            Button(onClick = onClick) {
                Text(buttonLabel)
            }
        }
    }
}

/**
 * 指定URIをACTION_VIEWで起動して、OS経由のディープリンク遷移を発火します。
 */
private fun openDeepLink(context: android.content.Context, uri: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
    context.startActivity(intent)
}

/**
 * DeepLinkScreenのプレビュー。
 */
@Preview(showBackground = true)
@Composable
private fun DeepLinkScreenPreview() {
    AndroidLabTheme {
        DeepLinkScreen()
    }
}
