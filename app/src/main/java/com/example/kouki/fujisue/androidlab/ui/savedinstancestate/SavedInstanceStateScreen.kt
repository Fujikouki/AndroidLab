package com.example.kouki.fujisue.androidlab.ui.savedinstancestate

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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedInstanceStateScreen(viewModel: SavedStateViewModel = viewModel()) {
    // ViewModelから状態を取得
    val vmCount by viewModel.vmCount.collectAsState()
    val plainVmCount by viewModel.plainVmCount.collectAsState()

    // remember: 再コンポーズ間で状態を保持するが、Activityの再生成では保持されない
    var rememberCount by remember { mutableStateOf(0) }

    // rememberSaveable: Activityの再生成後も状態を保持する
    var saveableCount by rememberSaveable { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("状態保存の比較") },
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
            // remember
            StateCard(
                title = "remember",
                description = "再コンポーズ間でのみ状態を保持します。画面回転などでリセットされます。",
                count = rememberCount,
                onIncrement = { rememberCount++ }
            )

            // rememberSaveable
            StateCard(
                title = "rememberSaveable",
                description = "画面回転やプロセスの再作成後も状態を保持します。",
                count = saveableCount,
                onIncrement = { saveableCount++ }
            )

            // ViewModel without SavedStateHandle
            StateCard(
                title = "ViewModel (Plain)",
                description = "ViewModel内で状態を保持します。画面回転では維持されますが、プロセスの再作成でリセットされます。",
                count = plainVmCount,
                onIncrement = { viewModel.incrementPlainVmCount() }
            )

            // ViewModel with SavedStateHandle
            StateCard(
                title = "ViewModel & SavedStateHandle",
                description = "SavedStateHandleにより、プロセスの再作成後も状態を復元できます。最も堅牢な方法です。",
                count = vmCount,
                onIncrement = { viewModel.incrementVmCount() }
            )
        }
    }
}

/**
 * 各状態保存方法のカウンターと説明を表示する共通のカードUI
 */
@Composable
fun StateCard(title: String, description: String, count: Int, onIncrement: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Count: $count", style = MaterialTheme.typography.titleLarge)
                Button(onClick = onIncrement) {
                    Text("Increment")
                }
            }
        }
    }
}
