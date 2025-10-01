package com.example.kouki.fujisue.androidlab.ui.other

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kouki.fujisue.androidlab.ui.theme.AndroidLabTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtherScreen() {
    val context = LocalContext.current
    val tooltipState = rememberTooltipState(isPersistent = true)
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Other Components: Tooltip") },
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
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "アイコンを長押しするとツールチップが表示されます。",
                style = MaterialTheme.typography.bodyLarge
            )

            TooltipBox(
                positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
                tooltip = {
                    RichTooltip(
                        title = { Text("詳細情報") },
                        text = { Text("これはRichTooltipのサンプルです。タイトル、テキスト、アクションボタンを配置できます。") },
                        action = {
                            TextButton(
                                onClick = {
                                    scope.launch { tooltipState.dismiss() }
                                    Toast.makeText(context, "アクション実行！", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            ) {
                                Text("アクション")
                            }
                        }
                    )
                },
                state = tooltipState
            ) {
                IconButton(onClick = { /* 長押しで表示 */ }) {
                    Icon(
                        modifier = Modifier.size(50.dp),
                        imageVector = Icons.Default.Info,
                        contentDescription = "情報アイコン"
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OtherScreenPreview() {
    AndroidLabTheme {
        OtherScreen()
    }
}
