package com.example.kouki.fujisue.androidlab.ui.sceneview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.kouki.fujisue.androidlab.ui.theme.AndroidLabTheme
import io.github.sceneview.Scene
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes

/**
 * 3Dモデルなどを表示するためのSceneViewのサンプル画面です。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SceneviewScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SceneView Examples") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            val context = LocalContext.current

            // 1. エンジンとモデルローダーの初期化
            val engine = rememberEngine()
            val modelLoader = rememberModelLoader(engine)

            // 2. ノード（3Dオブジェクト）の作成
            // rememberNodesを使うことで、再描画時もオブジェクトを保持します
            val childNodes = rememberNodes {
                add(
                    ModelNode(
                        modelInstance = modelLoader.createModelInstance(
                            assetFileLocation = "model.glb"
                        ),
                        scaleToUnits = 1.0f, // モデルのサイズを1メートル（SceneViewの単位）に正規化
                    ).apply {
                        // 必要に応じて初期位置などを調整
                        position = io.github.sceneview.math.Position(
                            x = 0.0f,
                            y = -0.6f,
                            z = 0.0f
                        ) // カメラの少し奥に配置
                        isEditable = false // ユーザーによる回転・移動操作を許可
                    }
                )
            }

            val cameraNode = rememberCameraNode(engine = engine).apply {
                // カメラ操作
                isEditable = false
                isRotationEditable = false
                isPositionEditable = false
                isTouchable = false
            }

            // 3. Scene（ビュー）の表示
            Scene(
                modifier = Modifier.fillMaxSize(),
                engine = engine,
                modelLoader = modelLoader,
                childNodes = childNodes,
                cameraNode = cameraNode,
                // 背景色や環境光などをここで設定可能
                onFrame = { _ ->
                    // 毎フレームの更新処理が必要な場合はここに記述
                }
            )


        }

    }
}

@Preview(showBackground = true)
@Composable
fun SceneviewScreenPreview() {
    AndroidLabTheme {
        SceneviewScreen()
    }
}
