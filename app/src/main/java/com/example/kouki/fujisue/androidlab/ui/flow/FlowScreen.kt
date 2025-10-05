package com.example.kouki.fujisue.androidlab.ui.flow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "StateFlow: $state")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.incrementStateFlow() }) {
                Text("Increment StateFlow")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.triggerSharedFlow() }) {
                Text("Trigger SharedFlow")
            }
        }
    }
}
