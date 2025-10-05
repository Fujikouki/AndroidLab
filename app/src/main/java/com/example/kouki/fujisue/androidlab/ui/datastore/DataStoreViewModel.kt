package com.example.kouki.fujisue.androidlab.ui.datastore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * DataStoreRepository を介して設定値を読み書きする ViewModel。
 */
class DataStoreViewModel(private val repository: DataStoreRepository) : ViewModel() {

    /**
     * DataStore に保存された値を購読する StateFlow。
     */
    val userPreferences: StateFlow<UserPreferences> = repository.userPreferences.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UserPreferences()
    )

    /**
     * 表示名を保存する。
     */
    fun saveDisplayName(displayName: String) {
        viewModelScope.launch {
            repository.updateDisplayName(displayName)
        }
    }

    /**
     * 通知設定を保存する。
     */
    fun onNotificationsToggle(isEnabled: Boolean) {
        viewModelScope.launch {
            repository.updateNotificationsEnabled(isEnabled)
        }
    }

    /**
     * サンプル用に保存済みの値を初期化する。
     */
    fun clearAll() {
        viewModelScope.launch {
            repository.clearAll()
        }
    }
}

/**
 * DataStoreViewModel にリポジトリを差し込むための Factory。
 */
class DataStoreViewModelFactory(private val repository: DataStoreRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DataStoreViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DataStoreViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
