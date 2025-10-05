package com.example.kouki.fujisue.androidlab.ui.datastore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kouki.fujisue.androidlab.ui.theme.AndroidLabTheme
import kotlinx.coroutines.launch

/**
 * DataStore を使った簡単なユーザー設定の保存フローを学ぶための画面。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataStoreScreen() {
    val context = LocalContext.current
    val viewModel: DataStoreViewModel = viewModel(
        factory = DataStoreViewModelFactory(DataStoreRepository(context.userPreferencesDataStore))
    )
    val userPreferences by viewModel.userPreferences.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var displayNameInput by remember(userPreferences) {
        mutableStateOf(userPreferences.displayName)
    }

    val topAppBarState = rememberTopAppBarState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("DataStore のサンプル") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Jetpack DataStore を使ってプリファレンスを保存・再読み込みするサンプルです。",
                style = MaterialTheme.typography.bodyLarge
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = "ユーザー名", style = MaterialTheme.typography.titleMedium)
                    OutlinedTextField(
                        value = displayNameInput,
                        onValueChange = { displayNameInput = it },
                        label = { Text("表示名を入力") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            val trimmedName = displayNameInput.trim()
                            coroutineScope.launch {
                                if (trimmedName.isBlank()) {
                                    snackbarHostState.showSnackbar(message = "空文字は保存できません")
                                } else {
                                    viewModel.saveDisplayName(trimmedName)
                                    snackbarHostState.showSnackbar(
                                        message = "表示名を保存しました",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        })
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                val trimmedName = displayNameInput.trim()
                                coroutineScope.launch {
                                    if (trimmedName.isBlank()) {
                                        snackbarHostState.showSnackbar(message = "空文字は保存できません")
                                    } else {
                                        viewModel.saveDisplayName(trimmedName)
                                        snackbarHostState.showSnackbar(
                                            message = "表示名を保存しました",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                }
                            },
                            enabled = displayNameInput.isNotBlank()
                        ) {
                            Text("保存")
                        }
                        TextButton(onClick = { displayNameInput = userPreferences.displayName }) {
                            Text("リセット")
                        }
                    }
                }
            }

            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "通知の有効化", style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = "スイッチを切り替えて DataStore に保存します。",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Switch(
                        checked = userPreferences.notificationsEnabled,
                        onCheckedChange = { isEnabled ->
                            viewModel.onNotificationsToggle(isEnabled)
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = if (isEnabled) "通知を有効化しました" else "通知を無効化しました",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "保存済みの値", style = MaterialTheme.typography.titleMedium)
                    Text(text = "表示名: ${userPreferences.displayName.ifBlank { "未設定" }}")
                    Text(
                        text = "通知設定: ${if (userPreferences.notificationsEnabled) "ON" else "OFF"}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.clearAll()
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "保存内容をクリアしました",
                            duration = SnackbarDuration.Short
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("DataStore を初期化")
            }
        }
    }
}

/**
 * プレビュー用のスタブ UI。実際の DataStore とは接続しない。
 */
@Preview(showBackground = true)
@Composable
private fun DataStoreScreenPreview() {
    AndroidLabTheme {
        Scaffold { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("DataStore のサンプル")
                Text("プレビューでは固定データを表示します")
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("表示名: PreviewUser")
                        Text("通知設定: ON")
                    }
                }
            }
        }
    }
}
