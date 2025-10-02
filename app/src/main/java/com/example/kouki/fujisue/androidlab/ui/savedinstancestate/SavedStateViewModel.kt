package com.example.kouki.fujisue.androidlab.ui.savedinstancestate

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

/**
 * SavedInstanceStateScreenの状態を管理するViewModel。
 * SavedStateHandleを使用して、プロセスの再作成後も状態を復元します。
 */
class SavedStateViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    // "vm_count"というキーでカウンターの状態を公開します。
    val vmCount: StateFlow<Int> = savedStateHandle.getStateFlow(KEY_VM_COUNT, 0)

    /**
     * ViewModelのカウンターをインクリメントし、SavedStateHandleに保存します。
     */
    fun incrementVmCount() {
        val currentCount = vmCount.value
        savedStateHandle[KEY_VM_COUNT] = currentCount + 1
    }

    companion object {
        private const val KEY_VM_COUNT = "vm_count"
    }
}
