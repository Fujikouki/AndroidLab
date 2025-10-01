package com.example.kouki.fujisue.androidlab.ui.storage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    /**
     * すべてのTODOアイテムを取得し、変更を監視します。
     * 未完了のアイテムを上に、完了済みのアイテムを下に表示するように並べ替えます。
     */
    @Query("SELECT * FROM todo_table ORDER BY id DESC")
    fun getAllTodos(): Flow<List<TodoItem>>

    /**
     * 新しいTODOアイテムをデータベースに挿入します。
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(todo: TodoItem)

    /**
     * 既存のTODOアイテムを更新します（チェックON/OFFなどで使用）。
     */
    @Update
    suspend fun update(todo: TodoItem)

    /**
     * 指定されたTODOアイテムをデータベースから削除します。
     */
    @Delete
    suspend fun delete(todo: TodoItem)

    /**
     * 完了済みのすべてのTODOアイテムをデータベースから削除します。
     */
    @Query("DELETE FROM todo_table WHERE isCompleted = 1")
    suspend fun deleteCompleted()
}
