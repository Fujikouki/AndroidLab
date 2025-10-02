package com.example.kouki.fujisue.androidlab.ui.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * ライフサイクルイベントのログ情報を保持するデータクラス
 */
data class LifecycleLog(val event: Lifecycle.Event, val timestamp: String)

/**
 * ライフサイクルイベントのリストを管理するViewModel
 */
class LifecycleViewModel : ViewModel() {

    private val _lifecycleEvents = MutableStateFlow<List<LifecycleLog>>(emptyList())
    val lifecycleEvents: StateFlow<List<LifecycleLog>> = _lifecycleEvents

    /**
     * 新しいライフサイクルイベントをリストに追加します。
     */
    fun addEvent(event: Lifecycle.Event) {
        val timestamp = SimpleDateFormat("HH:mm:ss.SSS", Locale.JAPAN).format(Date())
        val newLog = LifecycleLog(event, timestamp)
        _lifecycleEvents.value = _lifecycleEvents.value + newLog
    }
}
