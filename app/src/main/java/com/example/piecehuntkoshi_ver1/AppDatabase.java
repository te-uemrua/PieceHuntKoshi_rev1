package com.example.piecehuntkoshi_ver1;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;import androidx.room.RoomDatabase;

// @Databaseアノテーションで、このクラスがデータベース本体であることを示す
// entities = {...}: このデータベースがどのエンティティ(テーブル)を持つかを指定
// version = 1: データベースのバージョン。今後テーブル構造を変えるときに数字を上げる
// exportSchema = false: スキーマ情報をファイルに出力しない設定（今回は不要）
@Database(entities = {PuzzleData.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // このデータベースがどのDAOを持っているかを定義する抽象メソッド
    // Roomがこのメソッドを自動的に実装してくれる
    public abstract PuzzleDao puzzleDao();

    // データベースのインスタンスをシングルトン（アプリ内で常に一つ）で管理するための記述
    private static volatile AppDatabase INSTANCE;

    // データベースのインスタンスを取得するための静的メソッド
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "puzzle_database")
                            // .allowMainThreadQueries() // ←これは最終手段。今は使わない
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
