package com.example.kouki.fujisue.androidlab.ui.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen() {
    val context = LocalContext.current
    var lastLocation by remember { mutableStateOf<Location?>(null) }
    var permissionStatus by remember { mutableStateOf("パーミッションの状態: 不明") }

    // FusedLocationProviderClientのインスタンスを取得
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    // パーミッションリクエストのためのランチャー
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // 精度の高い位置情報が許可された
                    permissionStatus = "パーミッションの状態: 高精度アクセスが許可されています"
                    // ここで位置情報の取得を開始できる
                }

                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // 大まかな位置情報のみ許可された
                    permissionStatus = "パーミッションの状態: 大まかなアクセスが許可されています"
                }

                else -> {
                    // 許可されなかった
                    permissionStatus = "パーミッションの状態: 拒否されました"
                }
            }
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GPS Location") },
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(elevation = CardDefaults.cardElevation(4.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("現在地の情報", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "緯度: ${lastLocation?.latitude ?: "取得中..."}",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "経度: ${lastLocation?.longitude ?: "取得中..."}",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(permissionStatus, style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = {
                locationPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }) {
                Text("位置情報へのアクセスを要求")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                // パーミッションチェックと最新の位置情報の取得
                requestLastLocation(context, fusedLocationClient) { location ->
                    lastLocation = location
                }
            }) {
                Text("最後に記録された位置情報を取得")
            }

            Spacer(modifier = Modifier.height(16.dp))

            LocationUpdatesButton(fusedLocationClient = fusedLocationClient) { location ->
                lastLocation = location
            }
        }
    }
}

/**
 * 位置情報の継続的な更新を制御するボタンとロジック
 */
@SuppressLint("MissingPermission")
@Composable
private fun LocationUpdatesButton(
    fusedLocationClient: FusedLocationProviderClient,
    onLocationUpdate: (Location) -> Unit
) {
    var isUpdating by remember { mutableStateOf(false) }

    val locationCallback = remember {
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let(onLocationUpdate)
            }
        }
    }

    // コンポーザブルが破棄されたときに位置情報の更新を停止する
    DisposableEffect(isUpdating) {
        if (isUpdating) {
            val locationRequest = LocationRequest.Builder(10000L).build()
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
        onDispose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    Button(onClick = { isUpdating = !isUpdating }) {
        Text(if (isUpdating) "位置情報の更新を停止" else "位置情報の継続的な更新を開始")
    }
}

/**
 * 最後に記録された位置情報を一度だけ取得する
 */
@SuppressLint("MissingPermission")
private fun requestLastLocation(
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    onSuccess: (Location) -> Unit
) {
    // ここでもパーミッションチェックを行うのがより安全ですが、今回は簡略化しています
    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
        location?.let(onSuccess)
    }
}
