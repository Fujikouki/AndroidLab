package com.example.kouki.fujisue.androidlab.ui.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kouki.fujisue.androidlab.ui.theme.AndroidLabTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ButtonScreen() {
    var clickCount by remember { mutableIntStateOf(0) }
    var isButtonEnabled by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Button Examples") },
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
            Text("クリック回数: $clickCount")

            Button(onClick = { clickCount++ }) {
                Icon(
                    Icons.Filled.ThumbUp,
                    contentDescription = "Like",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("基本的なボタン (Button)")
            }

            OutlinedButton(onClick = { clickCount++ }) {
                Text("枠線ボタン (OutlinedButton)")
            }

            TextButton(onClick = { clickCount++ }) {
                Text("テキストボタン (TextButton)")
            }

            FilledTonalButton(onClick = { clickCount++ }) {
                Text("トーンボタン (FilledTonalButton)")
            }

            ElevatedButton(onClick = { clickCount++ }) {
                Text("浮き出しボタン (ElevatedButton)")
            }

            Button(
                onClick = { clickCount++ },
                enabled = isButtonEnabled
            ) {
                Text(if (isButtonEnabled) "有効なボタン" else "無効なボタン")
            }

            Button(onClick = { isButtonEnabled = !isButtonEnabled }) {
                Text(if (isButtonEnabled) "ボタンを無効化" else "ボタンを有効化")
            }

            IconButton(onClick = { clickCount++ }) {
                Icon(Icons.Filled.Favorite, contentDescription = "Favorite")
            }

            FloatingActionButton(onClick = { clickCount++ }) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }

            ExtendedFloatingActionButton(
                icon = { Icon(Icons.Filled.Add, contentDescription = "Add") },
                text = { Text("追加") },
                onClick = { clickCount++ }
            )

            Button(
                onClick = { clickCount++ },
                shape = RoundedCornerShape(12.dp) // 丸角
            ) {
                Text("角丸ボタン")
            }

            Button(
                onClick = { clickCount++ },
                shape = CutCornerShape(topStart = 16.dp, bottomEnd = 16.dp) // カットコーナー
            ) {
                Text("カットコーナーボタン")
            }

            Button(
                onClick = { clickCount++ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )
            ) {
                Text("色を変更したボタン")
            }

            Button(
                onClick = { clickCount++ },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(60.dp)
            ) {
                Text("サイズを変更したボタン")
            }

            var selectedIndex by remember { mutableIntStateOf(0) }
            val options1 = listOf("Day", "Month", "Week")

            SingleChoiceSegmentedButtonRow {
                options1.forEachIndexed { index, label ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = options1.size
                        ),
                        onClick = { selectedIndex = index },
                        selected = index == selectedIndex,
                        label = { Text(label) }
                    )
                }
            }

            val selectedOptions = remember {
                mutableStateListOf(false, false, false)
            }
            val options2 = listOf("List", "ThumbUp", "Favorite")
            val icons = listOf(
                Icons.AutoMirrored.Filled.List,
                Icons.Default.ThumbUp,
                Icons.Default.Favorite
            )

            MultiChoiceSegmentedButtonRow {
                options2.forEachIndexed { index, label ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = options2.size
                        ),
                        checked = selectedOptions[index],
                        onCheckedChange = {
                            selectedOptions[index] = !selectedOptions[index]
                        },
                        icon = { SegmentedButtonDefaults.Icon(selectedOptions[index]) },
                        label = {
                            Icon(
                                imageVector = icons[index],
                                contentDescription = label
                            )
                        }
                    )
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonScreenPreview() {
    AndroidLabTheme {
        ButtonScreen()
    }
}
