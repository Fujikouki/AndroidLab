package com.example.kouki.fujisue.androidlab.ui.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs
import kotlin.math.roundToInt

// Normalization constants for progress indicators
private const val MAX_ACCELEROMETER = 20f // approx 2g
private const val MAX_GYROSCOPE = 10f // rad/s
private const val MAX_MAGNETIC_FIELD = 100f // μT

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
    var gravity by remember { mutableStateOf<FloatArray?>(null) }
    var geomagnetic by remember { mutableStateOf<FloatArray?>(null) }

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
                    if (SensorManager.getRotationMatrix(r, i, currentGravity, currentGeomagnetic)) {
                        val orientation = FloatArray(3)
                        SensorManager.getOrientation(r, orientation)
                        orientationValues =
                            orientation.map { Math.toDegrees(it.toDouble()).toFloat() }
                                .toFloatArray()
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    DisposableEffect(Unit) {
        listOfNotNull(accelerometerSensor, magneticFieldSensor, gyroscopeSensor).forEach { sensor ->
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "端末のセンサー値を視覚的に表示します。",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            if (accelerometerSensor != null) {
                item {
                    SensorDataCard(
                        title = "加速度センサー",
                        unit = "m/s²",
                        icon = Icons.Default.Build,
                        labels = listOf("X", "Y", "Z"),
                        values = accelerometerValues,
                        normalizer = { abs(it) / MAX_ACCELEROMETER }
                    )
                }
            }

            if (gyroscopeSensor != null) {
                item {
                    SensorDataCard(
                        title = "ジャイロスコープ",
                        unit = "rad/s",
                        icon = Icons.Default.Build,
                        labels = listOf("X", "Y", "Z"),
                        values = gyroscopeValues,
                        normalizer = { abs(it) / MAX_GYROSCOPE }
                    )
                }
            }

            if (magneticFieldSensor != null) {
                item {
                    SensorDataCard(
                        title = "磁力センサー",
                        unit = "μT",
                        icon = Icons.Default.Build,
                        labels = listOf("X", "Y", "Z"),
                        values = magneticFieldValues,
                        normalizer = { abs(it) / MAX_MAGNETIC_FIELD }
                    )
                }
            }

            if (accelerometerSensor != null && magneticFieldSensor != null) {
                item {
                    SensorDataCard(
                        title = "端末の向き",
                        unit = "°",
                        icon = Icons.Default.Build,
                        labels = listOf("方位角", "傾斜", "ロール"),
                        values = orientationValues,
                        valueFormatter = { it.roundToInt().toString() },
                        normalizer = { value ->
                            when (value) {
                                orientationValues[0] -> (value + 360) % 360 / 360f // Azimuth (0 to 360)
                                orientationValues[1] -> (value + 180) / 360f // Pitch (-180 to 180)
                                else -> (value + 90) / 180f // Roll (-90 to 90)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SensorDataCard(
    title: String,
    unit: String,
    icon: ImageVector,
    labels: List<String>,
    values: FloatArray,
    valueFormatter: (Float) -> String = { "%.2f".format(it) },
    normalizer: (Float) -> Float
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(Modifier.weight(1f))
                Text(text = "($unit)", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(Modifier.height(16.dp))

            if (values.isNotEmpty() && labels.size == values.size) {
                labels.forEachIndexed { index, label ->
                    val value = values[index]
                    SensorValueRow(
                        label = label,
                        value = value,
                        formattedValue = valueFormatter(value),
                        normalizedValue = normalizer(value)
                    )
                    if (index < labels.lastIndex) {
                        Spacer(Modifier.height(8.dp))
                    }
                }
            } else {
                Text("データを待っています...")
            }
        }
    }
}

@Composable
fun SensorValueRow(
    label: String,
    value: Float,
    formattedValue: String,
    normalizedValue: Float
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            modifier = Modifier.width(60.dp),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
        )
        LinearProgressIndicator(
            progress = { normalizedValue.coerceIn(0f, 1f) },
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
        )
        Text(
            text = formattedValue,
            modifier = Modifier
                .width(70.dp)
                .padding(start = 12.dp),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.secondary
            )
        )
    }
}
