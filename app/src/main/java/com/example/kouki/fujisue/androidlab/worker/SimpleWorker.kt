package com.example.kouki.fujisue.androidlab.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay

/**
 * シンプルなバックグラウンドタスクを実行するワーカー。
 * 5秒待機し、ログを出力します。
 */
class SimpleWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Log.d(TAG, "バックグラウンドタスクを開始します")

        try {
            // 5秒待機する（時間のかかる処理をシミュレート）
            delay(5000)
        } catch (e: CancellationException) {
            Log.w(TAG, "バックグラウンドタスクがキャンセルされました", e)
            // キャンセル例外を再スローして、WorkManagerにキャンセルを正しく伝えます
            throw e
        }

        Log.d(TAG, "バックグラウンドタスクが正常に完了しました")
        return Result.success()
    }

    companion object {
        private const val TAG = "SimpleWorker"
    }
}
