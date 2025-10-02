package com.example.kouki.fujisue.androidlab.ui.collapsing

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsingToolbarScreen() {
    // スクロール状態を監視・制御するための状態オブジェクトを作成します。
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    // ScaffoldにnestedScrollモディファイアを適用し、スクロールイベントをTopAppBarに連携させます。
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            // LargeTopAppBarは、スクロールに応じてサイズが変化するTopAppBarです。
            LargeTopAppBar(
                title = { Text("Collapsing Toolbar") },
                // scrollBehaviorをTopAppBarに渡すことで、スクロールと連動するようになります。
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ) { innerPadding ->
        // LazyColumnは、スクロール可能なコンテンツです。
        // innerPaddingを適用することで、コンテンツがTopAppBarの下に隠れないようにします。
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(100) { index ->
                Text(
                    text = "Item #$index",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
