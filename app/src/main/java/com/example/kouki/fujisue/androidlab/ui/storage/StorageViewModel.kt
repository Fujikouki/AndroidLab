package com.example.kouki.fujisue.androidlab.ui.storage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// --- ViewModel ---
class TodoViewModel(private val repository: StorageRepository) : ViewModel() {

    val allTodos: StateFlow<List<TodoItem>> = repository.allTodos.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun insert(text: String) = viewModelScope.launch {
        if (text.isNotBlank()) {
            repository.insert(TodoItem(text = text))
        }
    }

    fun toggleCompleted(todo: TodoItem) = viewModelScope.launch {
        repository.update(todo.copy(isCompleted = !todo.isCompleted))
    }

    fun delete(todo: TodoItem) = viewModelScope.launch {
        repository.delete(todo)
    }

    fun deleteCompleted() = viewModelScope.launch {
        repository.deleteCompleted()
    }
}

// --- ViewModel Factory ---
class TodoViewModelFactory(private val repository: StorageRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
