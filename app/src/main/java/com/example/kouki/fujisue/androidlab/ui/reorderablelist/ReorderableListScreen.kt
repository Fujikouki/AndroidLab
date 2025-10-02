package com.example.kouki.fujisue.androidlab.ui.reorderablelist

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Collections

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReorderableListScreen() {
    val items = remember {
        mutableStateListOf(
            "Item 1", "Item 2", "Item 3", "Item 4", "Item 5",
            "Item 6", "Item 7", "Item 8", "Item 9", "Item 10"
        )
    }

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    var draggedIndex by remember { mutableIntStateOf(-1) }
    var dragOffset by remember { mutableFloatStateOf(0f) }

    val reorderableState = remember(items, listState, scope) {
        ReorderableState(items, listState, scope)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ドラッグ＆ドロップで並べ替え") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            itemsIndexed(items, key = { _, item -> item }) { index, item ->
                val isBeingDragged = index == draggedIndex
                val currentOffset = if (isBeingDragged) dragOffset else 0f

                // rememberUpdatedStateを使って、常に最新のindexを保持する
                val currentIndex by rememberUpdatedState(index)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 16.dp)
                        .graphicsLayer {
                            translationY = currentOffset
                            shadowElevation = if (isBeingDragged) 8.dp.toPx() else 1.dp.toPx()
                        }
                        .pointerInput(Unit) {
                            detectDragGesturesAfterLongPress(
                                onDragStart = {
                                    // キャプチャされた古いindexの代わりに、最新のcurrentIndexを使用する
                                    draggedIndex = currentIndex
                                },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    dragOffset += dragAmount.y

                                    val newDraggedIndex = reorderableState.onDrag(
                                        draggedIndex = draggedIndex,
                                        totalDragOffset = dragOffset
                                    ) { adjustment ->
                                        dragOffset += adjustment
                                    }
                                    draggedIndex = newDraggedIndex
                                },
                                onDragEnd = {
                                    draggedIndex = -1
                                    dragOffset = 0f
                                },
                                onDragCancel = {
                                    draggedIndex = -1
                                    dragOffset = 0f
                                }
                            )
                        }
                ) {
                    Text(
                        text = item,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

private class ReorderableState(
    private val items: MutableList<String>,
    private val listState: LazyListState,
    private val scope: CoroutineScope
) {
    private val AUTO_SCROLL_SPEED = 30f

    fun onDrag(draggedIndex: Int, totalDragOffset: Float, adjustOffset: (Float) -> Unit): Int {
        if (draggedIndex == -1) return -1

        val draggedItemInfo =
            listState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == draggedIndex }
        var newDraggedIndex = draggedIndex

        if (draggedItemInfo != null) {
            // --- 自動スクロール --- 
            val itemTop = draggedItemInfo.offset + totalDragOffset
            val itemBottom = itemTop + draggedItemInfo.size
            val viewportTop = listState.layoutInfo.viewportStartOffset
            val viewportBottom = listState.layoutInfo.viewportEndOffset
            val scrollThreshold = (listState.layoutInfo.viewportSize.height * 0.1f)

            scope.launch {
                if (itemBottom > viewportBottom - scrollThreshold) {
                    listState.scrollBy(AUTO_SCROLL_SPEED) // 下へスクロール
                } else if (itemTop < viewportTop + scrollThreshold) {
                    listState.scrollBy(-AUTO_SCROLL_SPEED) // 上へスクロール
                }
            }

            // --- アイテムの入れ替え --- 
            val draggedItemCenter =
                draggedItemInfo.offset + totalDragOffset + (draggedItemInfo.size / 2)
            val targetItemInfo = listState.layoutInfo.visibleItemsInfo.firstOrNull {
                it.index != draggedIndex &&
                        draggedItemCenter >= it.offset &&
                        draggedItemCenter <= (it.offset + it.size)
            }

            if (targetItemInfo != null) {
                val from = draggedIndex
                val to = targetItemInfo.index
                Collections.swap(items, from, to)

                // ドラッグ対象のインデックスも更新
                newDraggedIndex = to

                // オフセットのジャンプを防ぐための補正
                val offsetDifference =
                    draggedItemInfo.offset.toFloat() - targetItemInfo.offset.toFloat()
                adjustOffset(offsetDifference)
            }
        }
        return newDraggedIndex
    }
}
