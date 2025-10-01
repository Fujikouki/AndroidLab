package com.example.kouki.fujisue.androidlab.ui.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * ライフサイクルイベントのリストを管理するViewModel
 */
class LifecycleViewModel : ViewModel() {

    // 外部には読み取り専用のStateFlowを公開し、ViewModel内でのみ値を変更できるようにする
    private val _lifecycleEvents = MutableStateFlow<List<String>>(emptyList())
    val lifecycleEvents: StateFlow<List<String>> = _lifecycleEvents

    /**
     * 新しいライフサイクルイベントをリストに追加します。
     */
    fun addEvent(event: Lifecycle.Event) {
        val timestamp = SimpleDateFormat("HH:mm:ss.SSS", Locale.JAPAN).format(Date())
        val newEvent = "$timestamp: ${event.name}"
        // 現在のリストに新しいイベントを追加した新しいリストを生成してStateFlowを更新
        _lifecycleEvents.value = _lifecycleEvents.value + newEvent
    }
}
