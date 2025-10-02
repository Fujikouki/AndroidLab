package com.example.kouki.fujisue.androidlab.ui.glance

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.text.Text

/**
 * GlanceウィジェットのUIを定義するクラス
 */
class CounterWidget : GlanceAppWidget() {
    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        provideContent {
            Text("Hello World")
        }
    }
}
