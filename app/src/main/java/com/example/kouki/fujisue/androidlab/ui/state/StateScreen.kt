package com.example.kouki.fujisue.androidlab.ui.state

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
