package com.example.eluxwear.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.compose.ui.graphics.Color
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    private lateinit var sensorManager: SensorManager
    private var isPermissionGranted = false
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()

        // Register the permission launcher
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            isPermissionGranted = isGranted
        }

        // Check if BODY_SENSORS permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BODY_SENSORS)
            == PackageManager.PERMISSION_GRANTED
        ) {
            isPermissionGranted = true
        } else {
            // Request the permission
            requestPermissionLauncher.launch(Manifest.permission.BODY_SENSORS)
        }

        // Set up the content
        setContent {
            MultiSensorApp(isPermissionGranted, firestore)
        }
    }
}

@Composable
fun MultiSensorApp(isPermissionGranted: Boolean, firestore: FirebaseFirestore) {
    var selectedRoom by remember { mutableStateOf("None") }

    if (isPermissionGranted) {
        val context = LocalContext.current
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // Heart Rate Sensor
        val heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
        if (heartRateSensor != null) {
            val heartRateListener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    if (event != null) {
                        val heartRate = event.values[0].toInt()

                        // Save heart rate to Firestore
                        val heartRateData = hashMapOf(
                            "sensor" to "Heart Rate",
                            "value" to heartRate,
                            "timestamp" to System.currentTimeMillis()
                        )

                        firestore.collection("sensor_data")
                            .add(heartRateData)
                            .addOnSuccessListener {
                                Log.d("Firestore", "Heart rate data added")
                            }
                            .addOnFailureListener { e ->
                                Log.w("Firestore", "Error adding heart rate data: ${e.message}")
                            }
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
            }

            DisposableEffect(Unit) {
                sensorManager.registerListener(
                    heartRateListener,
                    heartRateSensor,
                    SensorManager.SENSOR_DELAY_NORMAL
                )
                onDispose {
                    sensorManager.unregisterListener(heartRateListener)
                }
            }
        }

        // Light Sensor
        val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        if (lightSensor != null) {
            val lightListener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    if (event != null) {
                        val light = event.values[0]

                        // Save light data to Firestore
                        val lightData = hashMapOf(
                            "sensor" to "Light",
                            "value" to light,
                            "timestamp" to System.currentTimeMillis()
                        )

                        firestore.collection("sensor_data")
                            .add(lightData)
                            .addOnSuccessListener {
                                Log.d("Firestore", "Light data added")
                            }
                            .addOnFailureListener { e ->
                                Log.w("Firestore", "Error adding light data: ${e.message}")
                            }
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
            }

            DisposableEffect(Unit) {
                sensorManager.registerListener(
                    lightListener,
                    lightSensor,
                    SensorManager.SENSOR_DELAY_NORMAL
                )
                onDispose {
                    sensorManager.unregisterListener(lightListener)
                }
            }
        }
    }

    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Where are you now?",
                fontSize = 14.sp,
                color = Color.Black
            )
            // Room Selection Buttons
            Button(onClick = {
                selectedRoom = "Living Room"

                // Save room selection to Firestore
                val roomData = hashMapOf(
                    "type" to "Room Selection",
                    "room" to "Living Room",
                    "timestamp" to System.currentTimeMillis()
                )

                firestore.collection("sensor_data")
                    .add(roomData)
                    .addOnSuccessListener {
                        Log.d("Firestore", "Room selection added")
                    }
                    .addOnFailureListener { e ->
                        Log.w("Firestore", "Error adding room selection: ${e.message}")
                    }
            }) {
                Text(text = "Living Room")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                selectedRoom = "Study Room"


                // Save room selection to Firestore
                val roomData = hashMapOf(
                    "type" to "Room Selection",
                    "room" to "Study Room",
                    "timestamp" to System.currentTimeMillis()
                )

                firestore.collection("sensor_data")
                    .add(roomData)
                    .addOnSuccessListener {
                        Log.d("Firestore", "Room selection added")
                    }
                    .addOnFailureListener { e ->
                        Log.w("Firestore", "Error adding room selection: ${e.message}")
                    }
            }) {
                Text(text = "Study Room")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Display Selected Room
            Text(text = "You are now in: $selectedRoom", fontSize = 13.sp, color = Color.Black)
        }
    }
}
