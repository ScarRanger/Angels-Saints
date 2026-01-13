package com.rhinepereira.saints

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.rhinepereira.saints.data.remote.RetrofitClient
import com.rhinepereira.saints.data.repository.RemoteContentRepository
import com.rhinepereira.saints.navigation.NavGraph
import com.rhinepereira.saints.ui.theme.AngelsAndSaintsTheme

class MainActivity : ComponentActivity() {

    private var currentIntent by mutableStateOf<Intent?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentIntent = intent

        // Generate FCM Registration Token and Subscribe to Daily Readings
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
            Log.d("FCM", "Device Registration Token: ${task.result}")
        }
        
        FirebaseMessaging.getInstance().subscribeToTopic("daily_readings")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "Subscribed to daily_readings topic")
                }
            }

        val api = RetrofitClient.getApi(applicationContext)
        val repository = RemoteContentRepository(api)

        setContent {
            AngelsAndSaintsTheme {
                val context = LocalContext.current
                val navController = rememberNavController()
                
                // Handle Deep Linking from Push Notification
                LaunchedEffect(currentIntent) {
                    currentIntent?.let { intent ->
                        // 1. Handle explicit deep link URI (from 'link' property in FCM)
                        intent.data?.let { uri ->
                            navController.navigate(uri) {
                                launchSingleTop = true
                            }
                            intent.data = null
                        } ?: run {
                            // 2. Handle 'NAVIGATE_TO' extra (from 'data' payload in FCM)
                            val navigateTo = intent.getStringExtra("NAVIGATE_TO")
                            if (navigateTo == "daily-readings") {
                                navController.navigate("daily-readings-content") {
                                    launchSingleTop = true
                                }
                                intent.removeExtra("NAVIGATE_TO")
                            }
                        }
                    }
                }

                // Permission Launcher for Notifications (Android 13+)
                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGranted ->
                        if (isGranted) {
                            Log.d("Permissions", "Notification permission granted")
                        }
                    }
                )

                // Check and request permission on launch
                LaunchedEffect(Unit) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                }

                NavGraph(
                    navController = navController,
                    repository = repository
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        currentIntent = intent
    }
}
