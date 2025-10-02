package com.example.kouki.fujisue.androidlab.ui.glance

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

/**
 * ウィジェットをホストするためのAppWidgetReceiver
 */
class CounterWidgetReceiver : GlanceAppWidgetReceiver() {

    // このレシーバーが扱うGlanceAppWidgetのインスタンスを返す
    override val glanceAppWidget: GlanceAppWidget = CounterWidget()
}
