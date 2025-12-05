package com.example.kouki.fujisue.androidlab.ui.play

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kouki.fujisue.androidlab.ui.theme.AndroidLabTheme
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

// 色のプリセット
val RouletteColors = listOf(
    Color(0xFFE57373), Color(0xFF64B5F6), Color(0xFF81C784),
    Color(0xFFFFD54F), Color(0xFFBA68C8), Color(0xFF4DB6AC)
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmoothRouletteScreen() {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ルーレット") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        SmoothRouletteContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }
}

@Composable
private fun SmoothRouletteContent(
    modifier: Modifier = Modifier
) {
    val items = remember { mutableStateListOf("大吉", "中吉", "小吉", "凶") }
    val rotationAngle = remember { Animatable(0f) }
    var resultText by remember { mutableStateOf("スタートボタンを押してね") }
    val scope = rememberCoroutineScope()

    var newItemText by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    // ▼▼▼ Paintオブジェクトをキャッシュ（メモリ効率化・チラつき防止） ▼▼▼
    val textPaint = remember {
        Paint().apply {
            color = android.graphics.Color.WHITE
            textSize = 40f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.DEFAULT_BOLD
            isAntiAlias = true // アンチエイリアスを有効化
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.size(320.dp)
        ) {

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 20.dp)
                    .graphicsLayer {
                        rotationZ = rotationAngle.value
                    }
            ) {
                val wheelSize = size.minDimension
                val radius = wheelSize / 2f
                val centerOffset = Offset(wheelSize / 2f, wheelSize / 2f)
                val sweepAngle = 360f / items.size

                items.forEachIndexed { index, item ->
                    val startAngle = index * sweepAngle

                    drawArc(
                        color = RouletteColors[index % RouletteColors.size],
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = true,
                        topLeft = Offset(
                            (size.width - wheelSize) / 2f,
                            (size.height - wheelSize) / 2f
                        ),
                        size = Size(wheelSize, wheelSize)
                    )

                    // テキストを描画
                    drawIntoCanvas { canvas ->
                        val textAngle = startAngle + sweepAngle / 2f
                        val textRadius = radius * 0.7f
                        val angleRad = Math.toRadians(textAngle.toDouble())

                        val x = centerOffset.x + (textRadius * cos(angleRad)).toFloat()
                        val y = centerOffset.y + (textRadius * sin(angleRad)).toFloat()

                        canvas.nativeCanvas.drawText(item, x, y, textPaint)
                    }
                }
            }

            // 針（動かない）
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Indicator",
                tint = Color.Black,
                modifier = Modifier
                    .size(50.dp)
                    .offset(y = (-10).dp)
            )
        }

        Text(text = resultText, style = MaterialTheme.typography.headlineSmall)

        Button(
            onClick = {
                scope.launch {
                    val targetIndex = items.indices.random()
                    resultText = "抽選中..."

                    val anglePerItem = 360f / items.size
                    val targetItemCenterAngle = (targetIndex * anglePerItem) + (anglePerItem / 2f)
                    val angleToTop = 270f - targetItemCenterAngle

                    val currentRotation = rotationAngle.value
                    val baseRotation = (currentRotation - (currentRotation % 360f)) + 1800f
                    val targetRotation = baseRotation + angleToTop + 360f

                    rotationAngle.animateTo(
                        targetValue = targetRotation,
                        animationSpec = tween(3000, easing = FastOutSlowInEasing)
                    )

                    resultText = "結果: ${items[targetIndex]}"
                    // items.removeAt(targetIndex)
                }
            },
            enabled = !rotationAngle.isRunning
        ) {
            Text("スタート")
        }

        HorizontalDivider()

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = newItemText,
                onValueChange = { newItemText = it },
                label = { Text("項目を追加") },
                singleLine = true,
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    if (newItemText.isNotBlank()) {
                        items.add(newItemText)
                        newItemText = ""
                        focusManager.clearFocus()
                    }
                })
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    if (newItemText.isNotBlank()) {
                        items.add(newItemText)
                        newItemText = ""
                        focusManager.clearFocus()
                    }
                },
                modifier = Modifier.background(
                    MaterialTheme.colorScheme.secondaryContainer,
                    RoundedCornerShape(8.dp)
                )
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    }
}

@Preview
@Composable
private fun PreviewSmoothRouletteScreen() {
    AndroidLabTheme {
        SmoothRouletteScreen()
    }
}
