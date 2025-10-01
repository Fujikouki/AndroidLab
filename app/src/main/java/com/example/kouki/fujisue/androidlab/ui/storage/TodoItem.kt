package com.example.kouki.fujisue.androidlab.ui.storage

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * To-Doアイテムを表すエンティティクラス。
 */
@Entity(tableName = "todo_table")
data class TodoItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,
    val isCompleted: Boolean = false
)
