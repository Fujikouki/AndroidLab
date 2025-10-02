package com.example.kouki.fujisue.androidlab

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

class SecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = {
                    val resultIntent = Intent().apply {
                        putExtra("result_key", "This is a result from SecondActivity")
                    }
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                }) {
                    Text("元の画面に戻る")
                }
            }
        }
    }
}
