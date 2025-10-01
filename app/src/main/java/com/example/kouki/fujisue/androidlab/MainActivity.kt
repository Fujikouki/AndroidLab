package com.example.kouki.fujisue.androidlab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kouki.fujisue.androidlab.ui.animation.AnimationScreen
import com.example.kouki.fujisue.androidlab.ui.button.ButtonScreen
import com.example.kouki.fujisue.androidlab.ui.camera.CameraScreen
import com.example.kouki.fujisue.androidlab.ui.dialog.DialogScreen
import com.example.kouki.fujisue.androidlab.ui.image.ImageScreen
import com.example.kouki.fujisue.androidlab.ui.input.InputScreen
import com.example.kouki.fujisue.androidlab.ui.layout.LayoutsScreen
import com.example.kouki.fujisue.androidlab.ui.list.ListScreen
import com.example.kouki.fujisue.androidlab.ui.main.MainScreen
import com.example.kouki.fujisue.androidlab.ui.navigation.Route
import com.example.kouki.fujisue.androidlab.ui.notification.NotificationScreen
import com.example.kouki.fujisue.androidlab.ui.other.OtherScreen
import com.example.kouki.fujisue.androidlab.ui.permission.PermissionsScreen
import com.example.kouki.fujisue.androidlab.ui.sideeffect.SideEffectScreen
import com.example.kouki.fujisue.androidlab.ui.text.TextScreen
import com.example.kouki.fujisue.androidlab.ui.theme.AndroidLabTheme
import com.example.kouki.fujisue.androidlab.ui.theming.ThemeMode
import com.example.kouki.fujisue.androidlab.ui.theming.ThemingScreen
import com.example.kouki.fujisue.androidlab.ui.touching.TouchingScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppContent()
        }
    }
}

@Composable
fun AppContent() {
    var themeMode by remember { mutableStateOf(ThemeMode.System) }
    val useDynamicColor = remember { mutableStateOf(true) }

    val isDark = when (themeMode) {
        ThemeMode.System -> isSystemInDarkTheme()
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
    }

    AndroidLabTheme(
        darkTheme = isDark,
        dynamicColor = useDynamicColor.value
    ) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Route.Main) {
            composable<Route.Main> {
                MainScreen(navController = navController)
            }
            composable<Route.TextScreen> {
                TextScreen()
            }
            composable<Route.ButtonScreen> {
                ButtonScreen()
            }
            composable<Route.ImageScreen> {
                ImageScreen()
            }
            composable<Route.ListScreen> {
                ListScreen()
            }
            composable<Route.DialogScreen> {
                DialogScreen()
            }
            composable<Route.InputScreen> {
                InputScreen()
            }
            composable<Route.LayoutsScreen> {
                LayoutsScreen()
            }
            composable<Route.StateManagementScreen> {
                // TODO: StateManagementScreenを実装する
                Text("StateManagementScreen")
            }
            composable<Route.PermissionsScreen> {
                PermissionsScreen()
            }
            composable<Route.NetworkingScreen> {
                // TODO: NetworkingScreenを実装する
                Text("NetworkingScreen")
            }
            composable<Route.StorageScreen> {
                // TODO: StorageScreenを実装する
                Text("StorageScreen")
            }
            composable<Route.AnimationScreen> {
                AnimationScreen()
            }
            composable<Route.ThemingScreen> {
                ThemingScreen(
                    currentThemeMode = themeMode,
                    isDynamicColorEnabled = useDynamicColor.value,
                    onThemeModeChange = { themeMode = it },
                    onDynamicColorToggle = { useDynamicColor.value = it }
                )
            }
            composable<Route.NotificationScreen> {
                NotificationScreen()
            }
            composable<Route.OtherScreen> {
                OtherScreen()
            }
            composable<Route.TouchingScreen> {
                TouchingScreen()
            }
            composable<Route.SideEffectScreen> {
                SideEffectScreen()
            }
            composable<Route.CameraScreen> {
                CameraScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppContent()
}
