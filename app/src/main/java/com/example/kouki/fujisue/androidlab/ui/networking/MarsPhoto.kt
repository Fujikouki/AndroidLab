package com.example.kouki.fujisue.androidlab.ui.networking

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// --- Data Model ---
@Serializable
data class MarsPhoto(
    val id: String?,
    @SerialName("img_src") // JSONのキー名と変数名をマッピング
    val imgSrc: String?
)
