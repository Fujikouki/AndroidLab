package com.example.kouki.fujisue.androidlab.ui.glance

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.Text
import com.example.kouki.fujisue.androidlab.MainActivity
import com.example.kouki.fujisue.androidlab.SecondActivity

/**
 * GlanceウィジェットのUIを定義するクラス
 */
class CounterWidget : GlanceAppWidget() {

    companion object {
        // カウンターの値を保存するためのPreferences Key
        internal val countKey = intPreferencesKey("glance_counter_key")
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            // GlanceThemeでMaterial3のデザインを適用
            GlanceTheme {
                MyContent(
                    context = context
                )
            }
        }
    }

    @Composable
    private fun MyContent(
        context: Context
    ) {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(color = GlanceTheme.colors.background.getColor(context)),
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Where to?", modifier = GlanceModifier.padding(12.dp))
            Row(
                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {
                Button(
                    text = "メイン",
                    onClick = actionStartActivity<MainActivity>()
                )
                Spacer(
                    modifier = GlanceModifier.width(16.dp)
                )
                Button(
                    text = "サブ",
                    onClick = actionStartActivity<SecondActivity>()
                )
            }
        }
    }
}
