package com.example.kouki.fujisue.androidlab.ui.state

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private const val TAG = "StateScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StateScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Composeの状態について学ぶ") },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            StateAndNoStateExample()
            Spacer(modifier = Modifier.height(32.dp))
            StableAndUnstableExample()
        }
    }
}

@Composable
private fun StateAndNoStateExample() {
    Log.d(TAG, "StateAndNoStateExample recomposed")

    // Stateを持たない変数
    var countWithoutState = 0

    // Stateを持つ変数
    var countWithState by remember { mutableIntStateOf(0) }

    Text("Stateの有無による再描画の違い")

    // Stateを持たないカウンター
    Button(onClick = {
        countWithoutState++
        Log.d(TAG, "Button 1 clicked, countWithoutState: $countWithoutState")
    }) {
        Log.d(TAG, "Button 1 recomposed")
        Text("Counter without state: $countWithoutState")
    }

    // Stateを持つカウンター
    Button(onClick = {
        countWithState++
        Log.d(TAG, "Button 2 clicked, countWithState: $countWithState")
    }) {
        Log.d(TAG, "Button 2 recomposed")
        Text("Counter with state: $countWithState")
    }
}

// 不安定なデータクラス
private class UnstableData(var name: String)

// 安定なデータクラス
private data class StableData(val name: String)

@Composable
private fun StableAndUnstableExample() {
    Log.d(TAG, "StableAndUnstableExample recomposed")

    val unstableData = remember { UnstableData("initial") }
    val stableData = remember { StableData("initial") }

    var recompositionTrigger by remember { mutableIntStateOf(0) }

    Text("安定性の違いによる再描画の違い")
    Button(onClick = { recompositionTrigger++ }) {
        Text("Trigger Recomposition")
    }

    // このTextで状態を読み取ることで、再コンポーズがトリガーされることを保証します。
    Text(text = "Trigger count: $recompositionTrigger")

    UnstableComposable(data = unstableData)
    StableComposable(data = stableData)
}

@Composable
private fun UnstableComposable(data: UnstableData) {
    Log.d(TAG, "UnstableComposable recomposed with data: ${data.name}")
    Text(text = "Unstable: ${data.name}")
}

@Composable
private fun StableComposable(data: StableData) {
    Log.d(TAG, "StableComposable recomposed with data: ${data.name}")
    Text(text = "Stable: ${data.name}")
}
