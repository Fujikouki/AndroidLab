package com.example.kouki.fujisue.androidlab.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.kouki.fujisue.androidlab.ui.theme.AndroidLabTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogScreen() {
    var showSimpleDialog by remember { mutableStateOf(false) }
    var showIconDialog by remember { mutableStateOf(false) }
    var showCustomDialog by remember { mutableStateOf(false) }
    var dialogResult by remember { mutableStateOf("なし") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dialog Examples") },
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("ダイアログの操作結果: $dialogResult")

            Button(onClick = { showSimpleDialog = true }) {
                Text("シンプルなダイアログを表示")
            }

            Button(onClick = { showIconDialog = true }) {
                Text("アイコン付きダイアログを表示")
            }

            Button(onClick = { showCustomDialog = true }) {
                Text("カスタムダイアログを表示")
            }
        }
    }

    // シンプルなAlertDialog
    if (showSimpleDialog) {
        AlertDialog(
            onDismissRequest = { showSimpleDialog = false },
            title = {
                Text(text = "操作の確認")
            },
            text = {
                Text("この操作を実行してもよろしいですか？")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        dialogResult = "「OK」が選択されました"
                        showSimpleDialog = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        dialogResult = "「キャンセル」が選択されました"
                        showSimpleDialog = false
                    }
                ) {
                    Text("キャンセル")
                }
            }
        )
    }

    // アイコン付きAlertDialog
    if (showIconDialog) {
        AlertDialog(
            onDismissRequest = { showIconDialog = false },
            icon = { Icon(Icons.Default.Info, contentDescription = "Information") },
            title = {
                Text(text = "お知らせ")
            },
            text = {
                Text("バージョン2.0がリリースされました。")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        dialogResult = "アイコン付きダイアログを閉じました"
                        showIconDialog = false
                    }
                ) {
                    Text("確認")
                }
            }
        )
    }

    // カスタムDialog
    if (showCustomDialog) {
        Dialog(onDismissRequest = { showCustomDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "カスタムダイアログ",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "このダイアログのコンテンツは、CardとColumnを使って自由に構成されています。",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    TextButton(
                        onClick = {
                            dialogResult = "カスタムダイアログを閉じました"
                            showCustomDialog = false
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("閉じる")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DialogScreenPreview() {
    AndroidLabTheme {
        DialogScreen()
    }
}
