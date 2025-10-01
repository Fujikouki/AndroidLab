package com.example.kouki.fujisue.androidlab.ui.touching

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.kouki.fujisue.androidlab.ui.theme.AndroidLabTheme
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TouchingScreen() {
    var tapOffset by remember { mutableStateOf(Offset.Zero) }
    var eventMessage by remember { mutableStateOf("画面のどこかをタップしてください") }

    // イベントを発火させる特定の領域 (x: 350-650, y: 650-950 の範囲)
    val targetRect = Rect(center = Offset(500f, 800f), radius = 150f)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Touching Example") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        tapOffset = offset
                        eventMessage = if (targetRect.contains(offset)) {
                            "ターゲットエリアがタップされました！"
                        } else {
                            "ターゲットエリアの外側です"
                        }
                    }
                }
                .drawBehind {
                    // ターゲットエリアを視覚的に表示
                    drawRect(
                        color = Color.Red.copy(alpha = 0.3f),
                        topLeft = targetRect.topLeft,
                        size = targetRect.size
                    )
                },
            contentAlignment = Alignment.Center
        ) {

            var offsetX by remember { mutableFloatStateOf(0f) }
            var offsetY by remember { mutableFloatStateOf(0f) }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "タップ座標: X=${tapOffset.x.toInt()}, Y=${tapOffset.y.toInt()}",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = eventMessage,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = "青い四角形をドラッグして移動してください",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = "ドラッグ開始座標: X=${offsetX.toInt()}, Y=${offsetY.toInt()}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            Box(
                Modifier
                    .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                    .background(Color.Blue)
                    .size(50.dp)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x
                            offsetY += dragAmount.y
                        }
                    }
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun TouchingScreenPreview() {
    AndroidLabTheme {
        TouchingScreen()
    }
}
