package com.example.kouki.fujisue.androidlab.ui.storage

import kotlinx.coroutines.flow.Flow

/**
 * UIとデータソース（DAO）の間のやり取りを仲介するリポジトリクラス。
 */
class StorageRepository(private val todoDao: TodoDao) {
    val allTodos: Flow<List<TodoItem>> = todoDao.getAllTodos()

    suspend fun insert(todo: TodoItem) {
        todoDao.insert(todo)
    }

    suspend fun update(todo: TodoItem) {
        todoDao.update(todo)
    }

    suspend fun delete(todo: TodoItem) {
        todoDao.delete(todo)
    }

    suspend fun deleteCompleted() {
        todoDao.deleteCompleted()
    }
}
