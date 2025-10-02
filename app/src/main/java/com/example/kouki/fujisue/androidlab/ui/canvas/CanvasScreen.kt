package com.example.kouki.fujisue.androidlab.ui.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CanvasScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("カスタム描画とCanvas") },
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
        ) {
            Text(
                text = "Canvasコンポーザブルを使い、基本的な図形を描画するサンプルです。",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Canvasコンポーザブルを配置します。
            // Modifier.aspectRatio(1f)で、Canvasを正方形に保ちます。
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(color = Color.White)
            ) {
                // DrawScope内で、描画コマンドを実行します。

                // 円を描画 (drawCircle)
                // - color: 色
                // - radius: 半径
                // - center: 中心の座標
                drawCircle(
                    color = Color.Red,
                    radius = size.minDimension / 4,
                    center = center
                )

                // 四角形を描画 (drawRect)
                // - color: 色
                // - topLeft: 左上の座標
                // - size: 幅と高さ
                drawRect(
                    color = Color.Blue,
                    topLeft = Offset(x = size.width / 8, y = size.height / 8),
                    size = Size(width = size.width / 4, height = size.height / 4)
                )

                // 枠線付きの円を描画
                // styleにStrokeを指定すると、塗りつぶしではなく線画になります。
                drawCircle(
                    color = Color.Green,
                    radius = size.minDimension / 3,
                    center = center,
                    style = Stroke(width = 8.dp.toPx())
                )

                // 線を描画 (drawLine)
                // - color: 色
                // - start: 開始点の座標
                // - end: 終了点の座標
                // - strokeWidth: 線の太さ
                drawLine(
                    color = Color.Magenta,
                    start = Offset(x = 0f, y = 0f),
                    end = Offset(x = size.width, y = size.height),
                    strokeWidth = 4.dp.toPx()
                )

                // 三角形を描画 (drawPath)
                // Pathを使い、3つの頂点を結んで三角形を描画します。
                val trianglePath = Path().apply {
                    // 1つ目の頂点（上）に移動
                    moveTo(size.width / 2f, size.height * 0.7f)
                    // 2つ目の頂点（左下）まで線を引く
                    lineTo(size.width * 0.2f, size.height - 20f)
                    // 3つ目の頂点（右下）まで線を引く
                    lineTo(size.width * 0.8f, size.height - 20f)
                    // パスを閉じて始点と終点を結ぶ
                    close()
                }
                drawPath(
                    path = trianglePath,
                    color = Color.Yellow
                )
            }
        }
    }
}
