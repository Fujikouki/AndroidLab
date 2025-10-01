package com.example.kouki.fujisue.androidlab.ui.networking

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

// --- Ktor Client ---
object KtorClient {
    // JSONパーサーをここで定義
    val jsonParser = Json { ignoreUnknownKeys = true }

    val httpClient = HttpClient(OkHttp) {
        // ContentNegotiationは今回は使われないが、他のAPIで必要になる可能性を考慮して残しておく
        install(ContentNegotiation) {
            json(jsonParser)
        }
    }
}

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

// --- UI ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetworkingScreen(viewModel: NetworkingViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Networking with Ktor") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Button(
                onClick = { viewModel.fetchMarsPhotos() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("火星の画像を取得")
            }

            when (val state = uiState) {
                is MarsUiState.Initial -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("上のボタンを押して、火星の画像を取得してください。")
                    }
                }

                is MarsUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is MarsUiState.Success -> {
                    PhotosGridScreen(photos = state.photos)
                }

                is MarsUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = state.message, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

@Composable
fun PhotosGridScreen(photos: List<MarsPhoto>, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = photos, key = { photo -> photo.id!! }) { photo ->
            MarsPhotoCard(photo = photo)
        }
    }
}

@Composable
fun MarsPhotoCard(photo: MarsPhoto, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f), // 正方形にする
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(photo.imgSrc)
                .crossfade(true)
                .build(),
            contentDescription = "Mars Photo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NetworkingScreenPreview() {
    MaterialTheme {
        NetworkingScreen()
    }
}
