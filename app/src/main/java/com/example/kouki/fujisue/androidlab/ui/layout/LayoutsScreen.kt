package com.example.kouki.fujisue.androidlab.ui.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.kouki.fujisue.androidlab.ui.theme.AndroidLabTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayoutsScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Layout Examples") },
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
                .verticalScroll(rememberScrollState())
        ) {
            SectionTitle("Column: 縦に配置")
            ColumnExample()

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                thickness = DividerDefaults.Thickness,
                color = DividerDefaults.color
            )

            SectionTitle("Row: 横に配置")
            RowExample()

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                thickness = DividerDefaults.Thickness,
                color = DividerDefaults.color
            )

            SectionTitle("Box: 重ねて配置")
            BoxExample()

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                thickness = DividerDefaults.Thickness,
                color = DividerDefaults.color
            )

            SectionTitle("ConstraintLayout: 複雑な配置")
            ConstraintLayoutExample()

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun ColumnExample() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp), // 要素間のスペース
        horizontalAlignment = Alignment.CenterHorizontally // 水平方向の中央揃え
    ) {
        Text("Item 1")
        Text("Item 2 (長いテキスト)")
        Text("Item 3")
    }
}

@Composable
fun RowExample() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround, // スペースを均等に分配
        verticalAlignment = Alignment.CenterVertically // 垂直方向の中央揃え
    ) {
        Text("Item A")
        Text("Item B")
        Text("Item C")
    }
}

@Composable
fun BoxExample() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(horizontal = 16.dp)
            .background(Color.LightGray),
        contentAlignment = Alignment.Center // コンテンツを中央に配置
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.Blue)
        )
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(Color.Yellow)
                .align(Alignment.BottomEnd) // 親Boxの右下に配置
        )

        Box(
            modifier = Modifier
                .padding(start = 200.dp)
                .size(60.dp)
                .background(Color.Green)
                .align(Alignment.TopStart)
        )

        Box(
            modifier = Modifier
                .size(60.dp)
                .background(Color.Red)
                .align(Alignment.TopStart) // 親Boxの左上に配置
        )

        Text("Box Layout", color = Color.White)
    }
}

@Composable
fun ConstraintLayoutExample() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 16.dp)
            .background(Color.LightGray.copy(alpha = 0.5f))
    ) {
        // createRefs() で各Composableの参照を作成
        val (button, text1, text2) = createRefs()

        Button(
            onClick = { },
            modifier = Modifier.constrainAs(button) { // constrainAs Modifierで制約を定義
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(parent.start)
            }
        ) {
            Text("Button")
        }

        Text(
            "中央のテキスト",
            Modifier.constrainAs(text1) {
                centerTo(parent) // 親に対して中央に配置
            }
        )

        Text(
            "ボタンの右下に配置",
            Modifier.constrainAs(text2) {
                top.linkTo(button.bottom, margin = 8.dp)
                start.linkTo(button.end, margin = 8.dp)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LayoutsScreenPreview() {
    AndroidLabTheme {
        LayoutsScreen()
    }
}
