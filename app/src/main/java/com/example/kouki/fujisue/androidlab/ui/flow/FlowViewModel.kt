package com.example.kouki.fujisue.androidlab.ui.flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Flowを学ぶためのViewModel
 */
class FlowViewModel : ViewModel() {

    private val _stateFlow = MutableStateFlow(0)
    val stateFlow = _stateFlow.asStateFlow()

    private val _sharedFlow = MutableSharedFlow<Int>()
    val sharedFlow = _sharedFlow.asSharedFlow()

    /**
     * StateFlowの値をインクリメントします。
     */
    fun incrementStateFlow() {
        _stateFlow.value++
    }

    /**
     * SharedFlowに現在のStateFlowの値を送信します。
     */
    fun triggerSharedFlow() {
        viewModelScope.launch {
            _sharedFlow.emit(stateFlow.value)
        }
    }
}
