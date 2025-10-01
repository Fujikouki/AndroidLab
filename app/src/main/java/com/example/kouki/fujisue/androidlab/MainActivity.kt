package com.example.kouki.fujisue.androidlab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kouki.fujisue.androidlab.ui.button.ButtonScreen
import com.example.kouki.fujisue.androidlab.ui.main.MainScreen
import com.example.kouki.fujisue.androidlab.ui.navigation.Route
import com.example.kouki.fujisue.androidlab.ui.text.TextScreen
import com.example.kouki.fujisue.androidlab.ui.theme.AndroidLabTheme

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
    AndroidLabTheme {
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
                // TODO: ImageScreenを実装する
                Text("ImageScreen")
            }
            composable<Route.ListScreen> {
                // TODO: ListScreenを実装する
                Text("ListScreen")
            }
            composable<Route.DialogScreen> {
                // TODO: DialogScreenを実装する
                Text("DialogScreen")
            }
            composable<Route.InputScreen> {
                // TODO: InputScreenを実装する
                Text("InputScreen")
            }
            composable<Route.LayoutsScreen> {
                // TODO: LayoutsScreenを実装する
                Text("LayoutsScreen")
            }
            composable<Route.StateManagementScreen> {
                // TODO: StateManagementScreenを実装する
                Text("StateManagementScreen")
            }
            composable<Route.PermissionsScreen> {
                // TODO: PermissionsScreenを実装する
                Text("PermissionsScreen")
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
                // TODO: AnimationScreenを実装する
                Text("AnimationScreen")
            }
            composable<Route.ThemingScreen> {
                // TODO: ThemingScreenを実装する
                Text("ThemingScreen")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppContent()
}
