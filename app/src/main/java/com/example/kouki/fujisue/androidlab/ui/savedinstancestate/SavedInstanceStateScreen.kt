package com.example.kouki.fujisue.androidlab.ui.savedinstancestate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedInstanceStateScreen() {
    var text by rememberSaveable { mutableStateOf("") }
    var count by rememberSaveable { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("rememberSaveableの学習") },
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
                text = "rememberSaveableは、Activityの再生成（画面回転など）やプロセスの再作成後も状態を保持します。カウンターとテキストフィールドの値を変更し、端末を回転させてみてください。",
                style = MaterialTheme.typography.bodyLarge
            )

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("状態が保存されるテキスト") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("カウンター: $count", style = MaterialTheme.typography.headlineMedium)

            Button(onClick = { count++ }) {
                Text("カウンターを増やす")
            }
        }
    }
}
