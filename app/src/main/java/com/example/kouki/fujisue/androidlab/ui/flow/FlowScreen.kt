package com.example.kouki.fujisue.androidlab.ui.flow

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectLatest

/**
 * StateFlowとSharedFlowを学ぶための画面
 */
@Composable
fun FlowScreen(viewModel: FlowViewModel = viewModel()) {
    val state by viewModel.stateFlow.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.sharedFlow.collectLatest {
            snackbarHostState.showSnackbar("SharedFlow: $it")
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            FlowCard(
                title = "StateFlow",
                description = "現在の状態を保持し、新しいコレクターに最新の値をすぐに提供します。UIの状態管理に最適です。",
                value = "StateFlow: $state",
                buttonText = "Increment StateFlow",
                onButtonClick = { viewModel.incrementStateFlow() }
            )
            Spacer(modifier = Modifier.height(16.dp))
            FlowCard(
                title = "SharedFlow",
                description = "イベントを複数のコレクターにブロードキャストします。一度きりのイベント（例: Snackbar表示）に適しています。",
                value = "Trigger a one-time event",
                buttonText = "Trigger SharedFlow",
                onButtonClick = { viewModel.triggerSharedFlow() }
            )
        }
    }
}

@Composable
private fun FlowCard(
    title: String,
    description: String,
    value: String,
    buttonText: String,
    onButtonClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = value, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onButtonClick) {
                Text(buttonText)
            }
        }
    }
}
