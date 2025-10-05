package com.example.kouki.fujisue.androidlab.ui.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * DataStore に保存されたユーザー設定を読み書きするためのリポジトリクラス。
 */
class DataStoreRepository(private val dataStore: DataStore<Preferences>) {
    private val displayNameKey = stringPreferencesKey("display_name")
    private val notificationsEnabledKey = booleanPreferencesKey("notifications_enabled")

    /**
     * DataStore から取り出したプリファレンスを UI 用のデータクラスに変換して公開する。
     */
    val userPreferences: Flow<UserPreferences> = dataStore.data
        .catch { throwable ->
            if (throwable is IOException) {
                emit(emptyPreferences())
            } else {
                throw throwable
            }
        }
        .map { preferences ->
            UserPreferences(
                displayName = preferences[displayNameKey].orEmpty(),
                notificationsEnabled = preferences[notificationsEnabledKey] ?: false
            )
        }

    /**
     * ユーザー名を保存する。
     */
    suspend fun updateDisplayName(displayName: String) {
        dataStore.edit { preferences ->
            preferences[displayNameKey] = displayName
        }
    }

    /**
     * 通知設定の ON/OFF を保存する。
     */
    suspend fun updateNotificationsEnabled(isEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[notificationsEnabledKey] = isEnabled
        }
    }

    /**
     * サンプル用に保存済みデータをクリアする。
     */
    suspend fun clearAll() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}

/**
 * UI が扱いやすいように整形した DataStore の値。
 */
data class UserPreferences(
    val displayName: String = "",
    val notificationsEnabled: Boolean = false
)

/**
 * Context から DataStore インスタンスを取得するためのエクステンション。
 */
val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_preferences"
)
