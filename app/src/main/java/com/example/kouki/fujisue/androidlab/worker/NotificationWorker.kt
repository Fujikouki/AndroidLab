package com.example.kouki.fujisue.androidlab.worker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.kouki.fujisue.androidlab.R

/**
 * 定期的に通知を表示するワーカー。
 */
class NotificationWorker(private val appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Log.d(TAG, "定期実行タスクを開始します")

        showNotification("WorkManager Demo", "定期的なバックグラウンドタスクが実行されました。")

        Log.d(TAG, "定期実行タスクが正常に完了しました")
        return Result.success()
    }

    /**
     * 通知を表示します。
     *
     * @param title 通知のタイトル
     * @param content 通知の本文
     */
    private fun showNotification(title: String, content: String) {
        // Android O以降では、通知を表示するために通知チャネルの登録が必要です。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "WorkManager Demo Channel"
            val descriptionText = "Channel for WorkManager demo notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // システムに通知チャネルを登録します。
            val notificationManager: NotificationManager =
                appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(appContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        // POST_NOTIFICATIONSパーミッションのチェック（API 33+）
        if (ActivityCompat.checkSelfPermission(
                appContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // パーミッションがない場合、ワーカーは通知を表示できません。
            // 実際のアプリでは、タスクをスケジュールする前にUI層でパーミッションを要求する必要があります。
            Log.e(TAG, "POST_NOTIFICATIONS permission not granted.")
            return
        }

        with(NotificationManagerCompat.from(appContext)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    companion object {
        private const val TAG = "NotificationWorker"
        private const val CHANNEL_ID = "workmanager_channel"
        private const val NOTIFICATION_ID = 1
    }
}
