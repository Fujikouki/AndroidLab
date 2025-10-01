package com.example.kouki.fujisue.androidlab

import android.app.Application
import com.example.kouki.fujisue.androidlab.ui.storage.AppDatabase

/**
 * アプリケーション全体で共有するインスタンスを保持するためのカスタムApplicationクラス。
 * このクラスは、アプリの起動時に一度だけ生成されます。
 */
class AndroidLabApplication : Application() {
    // by lazy を使うことで、このdatabase変数が初めてアクセスされたときにのみ
    // AppDatabase.getDatabase(this) が実行され、データベースのインスタンスが生成されます。
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}
