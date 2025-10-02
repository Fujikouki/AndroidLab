package com.example.kouki.fujisue.androidlab.ui.savedinstancestate

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * SavedInstanceStateScreenの状態を管理するViewModel。
 * SavedStateHandleを使用する場合としない場合の状態保持の違いを比較します。
 */
class SavedStateViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    // SavedStateHandleに保存されないカウンター。プロセスの再作成で失われます。
    private val _plainVmCount = MutableStateFlow(0)
    val plainVmCount: StateFlow<Int> = _plainVmCount.asStateFlow()

    // SavedStateHandleに保存されるカウンター。プロセスの再作成後も復元されます。
    val vmCount: StateFlow<Int> = savedStateHandle.getStateFlow(KEY_VM_COUNT, 0)

    /**
     * SavedStateHandleを使用しないカウンターをインクリメントします。
     */
    fun incrementPlainVmCount() {
        _plainVmCount.value++
    }

    /**
     * SavedStateHandleに保存されたカウンターをインクリメントします。
     */
    fun incrementVmCount() {
        val currentCount = vmCount.value
        savedStateHandle[KEY_VM_COUNT] = currentCount + 1
    }

    companion object {
        private const val KEY_VM_COUNT = "vm_count"
    }
}
