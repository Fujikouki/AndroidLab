package com.example.kouki.fujisue.androidlab.ui.storage

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kouki.fujisue.androidlab.AndroidLabApplication

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class) // ★ アニメーションのために追加
@Composable
fun StorageScreen() {
    val context = LocalContext.current
    val application = context.applicationContext as AndroidLabApplication
    val todoViewModel: TodoViewModel = viewModel(
        factory = TodoViewModelFactory(StorageRepository(application.database.todoDao()))
    )

    var newTodoText by remember { mutableStateOf("") }
    val todos by todoViewModel.allTodos.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TODO List with Room") },
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 入力エリア
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newTodoText,
                    onValueChange = { newTodoText = it },
                    label = { Text("新しいTODO") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    todoViewModel.insert(newTodoText)
                    newTodoText = ""
                }) {
                    Text("追加")
                }
            }

            Spacer(modifier = Modifier.padding(16.dp))

            if (todos.isEmpty()) {
                Text("まだTODOがありません。", modifier = Modifier.padding(top = 16.dp))
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(todos, key = { it.id }) { todo ->
                        TodoItemCard(
                            modifier = Modifier.animateItem(
                                fadeInSpec = null, fadeOutSpec = null,
                                placementSpec = spring(
                                    stiffness = Spring.StiffnessMediumLow,
                                    visibilityThreshold = IntOffset.VisibilityThreshold
                                )
                            ),
                            todo = todo,
                            onToggle = { todoViewModel.toggleCompleted(todo) },
                            onDelete = { todoViewModel.delete(todo) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.padding(16.dp))

            // 完了済みを削除するボタン
            if (todos.any { it.isCompleted }) {
                TextButton(onClick = { todoViewModel.deleteCompleted() }) {
                    Text("完了済みアイテムを削除")
                }
            }
        }
    }
}

@Composable
fun TodoItemCard(
    modifier: Modifier = Modifier,
    todo: TodoItem,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = modifier // ★ 外部から渡されたModifierを使用
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = todo.isCompleted,
                onCheckedChange = { onToggle() }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                modifier = Modifier.weight(1f), // ★ テキストが押し出されないようにweightを適用
                text = todo.text,
                textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else null,
                color = if (todo.isCompleted) Color.Gray else MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { onDelete() }) {
                Text("削除")
            }
        }
    }
}
