package com.example.kouki.fujisue.androidlab.ui.navigation

import kotlinx.serialization.Serializable

object Route {

    /** メイン画面 */
    @Serializable
    data object Main

    /** テキスト表示を試す画面 */
    @Serializable
    data object TextScreen

    /** ボタンのインタラクションを試す画面 */
    @Serializable
    data object ButtonScreen

    /** 画像の表示を試す画面 */
    @Serializable
    data object ImageScreen

    /** リスト表示を試す画面 */
    @Serializable
    data object ListScreen

    /** ダイアログ表示を試す画面 */
    @Serializable
    data object DialogScreen

    /** ユーザー入力を試す画面 (TextField, Checkbox, Switch, Sliderなど) */
    @Serializable
    data object InputScreen

    /** Jetpack Composeのレイアウト方法を学ぶ画面 (Column, Row, Box, ConstraintLayoutなど) */
    @Serializable
    data object LayoutsScreen

    /** 実行時パーミッションの扱い方を学ぶ画面 */
    @Serializable
    data object PermissionsScreen

    /** ネットワークリクエストとデータ表示を学ぶ画面 */
    @Serializable
    data object NetworkingScreen

    /** データ永続化 (SharedPreferences, Room) を学ぶ画面 */
    @Serializable
    data object StorageScreen

    /** Jetpack Composeのアニメーションを試す画面 */
    @Serializable
    data object AnimationScreen

    /** アプリのテーマ設定 (ライト/ダークテーマ、ダイナミックカラー) を学ぶ画面 */
    @Serializable
    data object ThemingScreen

    /**
     * 通知を学ぶ画面
     */
    @Serializable
    data object NotificationScreen

    /**
     * その他のUIコンポーネントを学ぶ画面
     */
    @Serializable
    data object OtherScreen

    /**
     *  タップ操作について学ぶ
     */
    @Serializable
    data object TouchingScreen

    /**
     * Composeの副作用について学ぶ
     */
    @Serializable
    data object SideEffectScreen

    /**
     * カメラ機能を学ぶ
     */
    @Serializable
    data object CameraScreen

    /**
     * GPS（位置情報）を学ぶ画面
     */
    @Serializable
    data object LocationScreen
}
