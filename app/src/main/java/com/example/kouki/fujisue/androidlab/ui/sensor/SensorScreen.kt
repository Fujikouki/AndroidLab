package com.example.kouki.fujisue.androidlab.ui.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SensorScreen() {
    val context = LocalContext.current
    val sensorManager = remember {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    // --- Sensor States ---
    var accelerometerValues by remember { mutableStateOf(floatArrayOf(0f, 0f, 0f)) }
    var magneticFieldValues by remember { mutableStateOf(floatArrayOf(0f, 0f, 0f)) }
    var gyroscopeValues by remember { mutableStateOf(floatArrayOf(0f, 0f, 0f)) }
    var orientationValues by remember { mutableStateOf(floatArrayOf(0f, 0f, 0f)) }
    var gravity = remember<FloatArray?> { null }
    var geomagnetic = remember<FloatArray?> { null }

    // --- Sensor Availability ---
    val accelerometerSensor = remember { sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }
    val magneticFieldSensor =
        remember { sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) }
    val gyroscopeSensor = remember { sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) }

    val sensorEventListener = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event ?: return
                when (event.sensor.type) {
                    Sensor.TYPE_ACCELEROMETER -> {
                        gravity = event.values.clone()
                        accelerometerValues = event.values.clone()
                    }

                    Sensor.TYPE_MAGNETIC_FIELD -> {
                        geomagnetic = event.values.clone()
                        magneticFieldValues = event.values.clone()
                    }

                    Sensor.TYPE_GYROSCOPE -> {
                        gyroscopeValues = event.values.clone()
                    }
                }

                val currentGravity = gravity
                val currentGeomagnetic = geomagnetic
                if (currentGravity != null && currentGeomagnetic != null) {
                    val r = FloatArray(9)
                    val i = FloatArray(9)
                    val success =
                        SensorManager.getRotationMatrix(r, i, currentGravity, currentGeomagnetic)
                    if (success) {
                        val orientation = FloatArray(3)
                        SensorManager.getOrientation(r, orientation)
                        orientationValues =
                            orientation.map { Math.toDegrees(it.toDouble()).toFloat() }
                                .toFloatArray()
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // Not used
            }
        }
    }

    DisposableEffect(Unit) {
        val sensorsToRegister =
            listOfNotNull(accelerometerSensor, magneticFieldSensor, gyroscopeSensor)
        sensorsToRegister.forEach { sensor ->
            sensorManager.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_UI
            )
        }

        onDispose {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sensor Examples") },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("センサーの値", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))

            // Accelerometer
            if (accelerometerSensor != null) {
                Text("加速度センサー (m/s^2)", style = MaterialTheme.typography.titleLarge)
                Text("X: ${accelerometerValues[0]}")
                Text("Y: ${accelerometerValues[1]}")
                Text("Z: ${accelerometerValues[2]}")
            } else {
                Text("加速度センサーは利用できません")
            }
            Spacer(Modifier.height(16.dp))

            // Gyroscope
            if (gyroscopeSensor != null) {
                Text("ジャイロスコープ (rad/s)", style = MaterialTheme.typography.titleLarge)
                Text("X: ${gyroscopeValues[0]}")
                Text("Y: ${gyroscopeValues[1]}")
                Text("Z: ${gyroscopeValues[2]}")
            } else {
                Text("ジャイロスコープは利用できません")
            }
            Spacer(Modifier.height(16.dp))

            // Magnetic Field
            if (magneticFieldSensor != null) {
                Text("磁力センサー (μT)", style = MaterialTheme.typography.titleLarge)
                Text("X: ${magneticFieldValues[0]}")
                Text("Y: ${magneticFieldValues[1]}")
                Text("Z: ${magneticFieldValues[2]}")
            } else {
                Text("磁力センサーは利用できません")
            }
            Spacer(Modifier.height(16.dp))

            // Orientation
            if (accelerometerSensor != null && magneticFieldSensor != null) {
                Text("端末の向き", style = MaterialTheme.typography.titleLarge)
                Text("方位角 (Azimuth): ${orientationValues[0].roundToInt()}°") // Z-axis rotation
                Text("傾斜 (Pitch): ${orientationValues[1].roundToInt()}°") // X-axis rotation
                Text("ロール (Roll): ${orientationValues[2].roundToInt()}°") // Y-axis rotation
            } else {
                Text("端末の向きは計算できません")
            }
        }
    }
}
