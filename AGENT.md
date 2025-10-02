# Agent Contribution Report

## Role

ペアプログラマー

## Feature Implementation Log

[このセクションには、AIアシスタントが実装または支援した具体的な機能のリストを記述します]

### UI & Layouts
- [例: `TextScreen` - `Text`コンポーザブルのスタイリングを実装]

### App Architecture
- [例: `StateManagementScreen` - `ViewModel`を導入し、状態管理ロジックをリファクタリング]

### Android API & Library Integration
- [例: `LocationScreen` - `FusedLocationProviderClient`を使ったGPS機能の実装]

### Project-Level Tasks
- [例: `README.md` - プロジェクトの概要と機能一覧を記述]

## 連携ルール (Rules of Engagement)

- **言語**: 会話は基本的に日本語で行います。
- **ライブラリ管理**: 依存関係の競合を避けるため、新しいライブラリの追加はユーザーが手動で行います。AIアシスタントは追加すべきライブラリを提案しますが、`build.gradle.kts`ファイルの直接編集は行いません。
- **UI構築**: UIの構築はJetpack Composeで行います。
- **コメント**: 作成した関数やクラスには、その役割を説明するコメントを記述します。
