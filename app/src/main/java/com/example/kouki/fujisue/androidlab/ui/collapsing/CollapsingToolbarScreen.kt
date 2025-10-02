package com.example.kouki.fujisue.androidlab.ui.collapsing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsingToolbarScreen() {
    // TopAppBarの状態を管理します
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    // LazyColumnのスクロール状態を管理します
    val listState = rememberLazyListState()
    // コルーチンを起動するために使用します
    val coroutineScope = rememberCoroutineScope()

    // FAB（フローティングアクションボタン）を表示するかどうかの状態を、リストのスクロール位置から派生させます。
    // 最初のアイテムが見えなくなったらFABを表示します。
    val isFabVisible by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text("Collapsing Toolbar") },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        },
        floatingActionButton = {
            // AnimatedVisibilityを使用して、FABの表示・非表示をアニメーションさせます。
            AnimatedVisibility(
                visible = isFabVisible,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                FloatingActionButton(
                    onClick = {
                        // FABをクリックすると、リストの先頭までスムーズにスクロールします。
                        coroutineScope.launch {
                            listState.animateScrollToItem(0)
                        }
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "リストの先頭にスクロール"
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            state = listState, // LazyColumnに状態を渡します
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
