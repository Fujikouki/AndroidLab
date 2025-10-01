package com.example.kouki.fujisue.androidlab.ui.sideeffect

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.kouki.fujisue.androidlab.ui.theme.AndroidLabTheme
import kotlinx.coroutines.delay

private const val TAG = "SideEffectScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SideEffectScreen() {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Side-Effect Examples") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Section(title = "LaunchedEffect") {
                LaunchedEffectExample(snackbarHostState)
            }
            HorizontalDivider(Modifier.padding(vertical = 16.dp))

            Section(title = "rememberUpdatedState") {
                RememberUpdatedStateExample()
            }
            HorizontalDivider(Modifier.padding(vertical = 16.dp))

            Section(title = "DisposableEffect") {
                DisposableEffectExample()
            }
            HorizontalDivider(Modifier.padding(vertical = 16.dp))

            Section(title = "produceState") {
                ProduceStateExample()
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun Section(title: String, content: @Composable () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        Card(modifier = Modifier.padding(8.dp)) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
            }
        }
    }
}

@Composable
private fun LaunchedEffectExample(snackbarHostState: SnackbarHostState) {
    var counter by remember { mutableIntStateOf(0) }

    // 'counter'が変更されるたびにスナックバーを表示するLaunchedEffect
    // 最初の表示時(counter=0)には実行したくないので、checkを追加
    LaunchedEffect(key1 = counter) {
        if (counter > 0) {
            // showSnackbar関数はsuspend関数なので、コルーチン内から呼び出す
            snackbarHostState.showSnackbar("Counter is now $counter")
        }
    }

    Text("ボタンを押すとLaunchedEffectがトリガーされます。")
    Button(onClick = { counter++ }) {
        Text("Counter: $counter")
    }
}

@Composable
private fun RememberUpdatedStateExample() {
    var message by remember { mutableStateOf("Hello") }
    // このTextは、`message`の最新の値を常に表示する
    var effectLog by remember { mutableStateOf("エフェクトはまだ実行されていません") }

    val updatedMessage by rememberUpdatedState(newValue = message)

    // このエフェクトはキーがUnitなので、画面の表示中ずっと実行される
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000) // 3秒待つ
            // rememberUpdatedStateを使ったことで、エフェクトを再開せずに最新の`message`を参照できる
            effectLog = "3秒前のメッセージ: ${updatedMessage}"
        }
    }

    Text("LaunchedEffect内の処理は、通常は再起動しない限り古い状態を参照します。rememberUpdatedStateを使うと、再起動せずに最新の値を参照できます。")
    Spacer(Modifier.height(8.dp))
    Text(text = effectLog, style = MaterialTheme.typography.bodyMedium)
    Spacer(Modifier.height(8.dp))
    Button(onClick = { message = if (message == "Hello") "Compose" else "Hello" }) {
        Text("メッセージを切り替え: $message")
    }
}

@Composable
private fun DisposableEffectExample() {
    var status by remember { mutableStateOf("リスナー未登録") }
    val lifecycleOwner = LocalLifecycleOwner.current

    // このコンポーザブルが表示されている間だけライフサイクルイベントを購読する
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                Log.d(TAG, "Lifecycle: ON_START")
                status = "リスナー登録済み (ON_START)"
            } else if (event == Lifecycle.Event.ON_STOP) {
                Log.d(TAG, "Lifecycle: ON_STOP")
                status = "リスナー一時停止 (ON_STOP)"
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        status = "リスナー登録中..."

        // コンポーザブルが破棄されるときに、リスナーを解除する
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            Log.d(TAG, "Lifecycle: Observer removed onDispose")
            // このログは、画面から離れたときなどに見ることができる
        }
    }
    Text("この画面が表示されている間、LifecycleObserverが登録されます。画面から離れると自動的に解除されます。")
    Spacer(Modifier.height(8.dp))
    Text(text = "状態: $status", style = MaterialTheme.typography.titleMedium)
}

@Composable
private fun ProduceStateExample() {
    // 3秒後にデータをロードする非同期処理をシミュレート
    val loadedData: State<String> = produceState(initialValue = "データをロード中...") {
        delay(3000)
        value = "データロード完了！"
    }

    Text("produceStateは、非同期処理の結果をStateに変換します。")
    Spacer(Modifier.height(8.dp))
    Text(text = loadedData.value, style = MaterialTheme.typography.titleMedium)
}

@Preview(showBackground = true)
@Composable
fun SideEffectScreenPreview() {
    AndroidLabTheme {
        SideEffectScreen()
    }
}
