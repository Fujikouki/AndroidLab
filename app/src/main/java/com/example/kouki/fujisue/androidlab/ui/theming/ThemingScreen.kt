package com.example.kouki.fujisue.androidlab.ui.theming

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kouki.fujisue.androidlab.ui.theme.AndroidLabTheme

enum class ThemeMode {
    System, Light, Dark
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemingScreen(
    currentThemeMode: ThemeMode,
    isDynamicColorEnabled: Boolean,
    onThemeModeChange: (ThemeMode) -> Unit,
    onDynamicColorToggle: (Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Theming Example") },
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
            ThemeOptions(currentThemeMode, onThemeModeChange)

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Dynamic ColorはAndroid 12 (S) 以降で利用可能
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                DynamicColorOption(isDynamicColorEnabled, onDynamicColorToggle)
            }
        }
    }
}

@Composable
private fun ThemeOptions(currentThemeMode: ThemeMode, onThemeModeChange: (ThemeMode) -> Unit) {
    val themeOptions = ThemeMode.values()

    Card {
        Column(Modifier.padding(vertical = 8.dp)) {
            Text(
                text = "テーマの選択",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Column(Modifier.selectableGroup()) {
                themeOptions.forEach { themeMode ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = (themeMode == currentThemeMode),
                                onClick = { onThemeModeChange(themeMode) },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (themeMode == currentThemeMode),
                            onClick = null // RowのonClickで処理
                        )
                        Text(
                            text = themeMode.name,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DynamicColorOption(
    isDynamicColorEnabled: Boolean,
    onDynamicColorToggle: (Boolean) -> Unit
) {
    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("ダイナミックカラー", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "壁紙の色をテーマに反映します (Android 12+)",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Switch(checked = isDynamicColorEnabled, onCheckedChange = onDynamicColorToggle)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ThemingScreenPreview() {
    AndroidLabTheme {
        ThemingScreen(
            currentThemeMode = ThemeMode.System,
            isDynamicColorEnabled = true,
            onThemeModeChange = {},
            onDynamicColorToggle = {}
        )
    }
}
