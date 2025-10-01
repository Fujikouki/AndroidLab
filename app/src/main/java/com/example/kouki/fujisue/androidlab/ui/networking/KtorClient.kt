package com.example.kouki.fujisue.androidlab.ui.networking

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
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
