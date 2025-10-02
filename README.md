# Android Lab

## 概要 (Overview)

`Android Lab` は、モダンなAndroidアプリケーション開発、特に**Jetpack Compose**のさまざまな機能を学習し、デモンストレーションするために作られたサンプルアプリです。

このアプリを通じて、UIコンポーネントの基本的な使い方から、ナビゲーション、状態管理、非同期処理、データベース連携まで、幅広いトピックを実践的に学ぶことができます。

## 主な機能と学習トピック (Features & Topics Covered)

このアプリは、以下の画面で構成されており、それぞれが特定の機能や概念を学ぶためのサンドボックスとなっています。

### UI & Layouts
- **TextScreen**: `Text`コンポーザブルの基本的な使い方とスタイリング。
- **ButtonScreen**: `Button`, `IconButton`などのインタラクティブな要素。
- **ImageScreen**: `Image`と、ネットワーク経由で画像を読み込むCoilライブラリの使い方。
- **InputScreen**: `TextField`, `Checkbox`, `Switch`, `Slider`など、ユーザーからの入力を受け取るコンポーネント。
- **LayoutsScreen**: `Column`, `Row`, `Box`, `ConstraintLayout`を使った、宣言的なUIレイアウトの構築。
- **ListScreen**: `LazyColumn`と`LazyVerticalGrid`を使った、効率的なスクロールリストの表示。
- **DialogScreen**: `AlertDialog`を使ったダイアログ表示。
- **AnimationScreen**: `animateItemPlacement`を使ったリストアイテムのアニメーション。
- **TouchingScreen**: ジェスチャー操作のハンドリング。

### アプリケーションアーキテクチャ (App Architecture)
- **StateManagementScreen**: `State`, `remember`, `ViewModel`の役割を比較し、Composeにおける状態管理の基本を学ぶ。
- **SideEffectScreen**: `LaunchedEffect`, `DisposableEffect`など、コンポジションのライフサイクル外で処理を行う「副作用」の安全な扱い方。
- **LifecycleScreen**: `ViewModel`と`DisposableEffect`を使って、ActivityやComposableのライフサイクルイベントを監視する方法。
- **Navigation**: `NavHost`と`rememberNavController`、そしてタイプセーフな`kotlinx.serialization`を使った画面遷移。
- **ThemingScreen**: ライト/ダークテーマの切り替えと、Android 12+のダイナミックカラー（Material You）への対応。

### Android API & ライブラリ連携 (Android APIs & Libraries)
- **PermissionsScreen**: `rememberLauncherForActivityResult`を使った、実行時パーミッションの要求。
- **NotificationScreen**: 通知チャンネルの作成と、通知の発行。
- **CameraScreen**: `rememberLauncherForActivityResult`でカメラを起動し、撮影した画像を画面に表示。
- **LocationScreen**: `FusedLocationProviderClient`を使い、GPSで現在地の緯度・経度を取得・表示。
- **NetworkingScreen**: **Ktor**ライブラリを使い、ネットワーク経由でリモートサーバーからJSONデータを取得・表示。
- **StorageScreen**: **Room**データベースを使い、TODOリストの追加・更新・削除を行う（データの永続化）。

## セットアップ (Setup)

1.  このリポジトリをクローンします。
2.  Android Studioでプロジェクトを開きます。
3.  ビルドして、エミュレータまたは実機にインストールします。

## 今後の展望 (Future Work)

このプロジェクトは、さらに多くの機能を実装するためのベースとして拡張可能です。

- **Hilt**の導入による依存性の注入（DI）
- **DataStore**を使った設定の永続化
- **Paging3**を使った大規模リストの表示
- ネットワークとデータベースを連携させたオフラインキャッシュ機能
