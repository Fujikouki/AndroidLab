package com.example.kouki.fujisue.androidlab.ui.activityresult

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.kouki.fujisue.androidlab.SecondActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityResultScreen() {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var resultText by remember { mutableStateOf("結果はここに表示されます") }
    val context = LocalContext.current

    // 別のActivityを起動して結果を受け取るランチャー
    val customActivityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data?.getStringExtra("result_key")
            resultText = "SecondActivityから受け取った結果: $data"
        } else {
            resultText = "SecondActivityがキャンセルされました"
        }
    }

    // 画像を選択するランチャー
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
        resultText =
            if (uri != null) "画像が選択されました: $uri" else "画像選択がキャンセルされました"
    }

    // ドキュメントを選択するランチャー
    val selectFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        resultText =
            if (uri != null) "ファイルが選択されました: $uri" else "ファイル選択がキャンセルされました"
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ActivityResult a la carte") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "rememberLauncherForActivityResultを使用して、別のアクティビティやコンテンツピッカーから結果を受け取ります。",
                style = MaterialTheme.typography.bodyLarge
            )

            Button(onClick = {
                val intent = Intent(context, SecondActivity::class.java)
                customActivityLauncher.launch(intent)
            }) {
                Text("別Activityを起動")
            }

            Button(onClick = { pickImageLauncher.launch("image/*") }) {
                Text("画像を選択")
            }

            Button(onClick = { selectFileLauncher.launch(arrayOf("*/*")) }) {
                Text("ファイルを選択")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = resultText, style = MaterialTheme.typography.bodyLarge)

            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "選択された画像",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
