package com.example.kouki.fujisue.androidlab.ui.image

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.kouki.fujisue.androidlab.R
import com.example.kouki.fujisue.androidlab.ui.theme.AndroidLabTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Image Examples") },
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
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("ローカルリソース画像 (Image)")
            Image(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = "Local Image",
                modifier = Modifier.size(120.dp)
            )

            Text("円形にクリップした画像")
            Image(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = "Circular Image",
                contentScale = ContentScale.Crop, // 画像がコンテナを埋めるようにトリミング
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )

            Text("角丸にクリップした画像")
            Image(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = "Rounded Corner Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(16.dp))
            )

            Text("枠線付きの円形画像")
            Image(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = "Image with Border",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(4.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )

            Text("虹色にグラデーションする円の枠線")
            val rainbowColorsBrush = remember {
                Brush.sweepGradient(
                    listOf(
                        Color(0xFF9575CD),
                        Color(0xFFBA68C8),
                        Color(0xFFE57373),
                        Color(0xFFFFB74D),
                        Color(0xFFFFF176),
                        Color(0xFFAED581),
                        Color(0xFF4DD0E1),
                        Color(0xFF9575CD)
                    )
                )
            }
            val borderWidth = 4.dp
            Image(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = "グラデーション",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp)
                    .border(
                        BorderStroke(borderWidth, rainbowColorsBrush),
                        CircleShape
                    )
                    .padding(borderWidth)
                    .clip(CircleShape)
            )

            Text("カラー マトリックスを使用(彩度0)")
            Image(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = "カラー マトリックスを使用(彩度0)",
                colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
            )

            Text("ぼかし効果")
            Image(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = "ぼかし効果",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp)
                    .blur(
                        radiusX = 10.dp,
                        radiusY = 10.dp,
                        edgeTreatment = BlurredEdgeTreatment(RoundedCornerShape(8.dp))
                    )
            )

            Text("ContentScale.Fit (全体表示)")
            Image(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = "ContentScale.Fit",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(150.dp, 100.dp)
                    .border(1.dp, MaterialTheme.colorScheme.outline)
            )

            Text("ContentScale.Crop (トリミング)")
            Image(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = "ContentScale.Crop",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp, 100.dp)
                    .border(1.dp, MaterialTheme.colorScheme.outline)
            )

            Text("ContentScale.FillBounds (引き伸ばし)")
            Image(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = "ContentScale.FillBounds",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(150.dp, 100.dp)
                    .border(1.dp, MaterialTheme.colorScheme.outline)
            )

            Text("ネットワーク画像 (Coil AsyncImage)")
            AsyncImage(
                model = "https://picsum.photos/200/300",
                contentDescription = "Network Image",
                modifier = Modifier.size(150.dp, 100.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ImageScreenPreview() {
    AndroidLabTheme {
        ImageScreen()
    }
}
