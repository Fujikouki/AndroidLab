package com.example.kouki.fujisue.androidlab.ui.networking

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// --- ViewModel ---
sealed interface MarsUiState {
    data object Initial : MarsUiState
    data object Loading : MarsUiState
    data class Success(val photos: List<MarsPhoto>) : MarsUiState
    data class Error(val message: String) : MarsUiState
}

class NetworkingViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<MarsUiState>(MarsUiState.Initial)
    val uiState: StateFlow<MarsUiState> = _uiState.asStateFlow()

    fun fetchMarsPhotos() {
        viewModelScope.launch {
            _uiState.value = MarsUiState.Loading
            try {
                // 1. レスポンスをまずStringとして受け取る
                val responseText =
                    KtorClient.httpClient.get("https://android-kotlin-fun-mars-server.appspot.com/photos")
                        .bodyAsText()
                // 2. 受け取ったStringを手動でデコードする
                val photos = KtorClient.jsonParser.decodeFromString<List<MarsPhoto>>(responseText)

                // imgSrcがnullや空でない、有効なデータのみをUIに渡す
                val validPhotos =
                    photos.filter { !it.id.isNullOrBlank() && !it.imgSrc.isNullOrBlank() }
                _uiState.value = MarsUiState.Success(validPhotos)

            } catch (e: Exception) {
                Log.e("NetworkingViewModel", "Error fetching Mars photos", e)
                _uiState.value = MarsUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}
