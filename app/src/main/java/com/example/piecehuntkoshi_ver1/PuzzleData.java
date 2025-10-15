package com.example.piecehuntkoshi_ver1;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// @Entityアノテーションで、このクラスが"puzzle_pieces"という名前のテーブルであることを示す
@Entity(tableName = "puzzle_pieces")
public class PuzzleData {

    // @PrimaryKeyで、この'id'がテーブルの主キー（各行を一位に識別するID）であることを示す
    @PrimaryKey
    @ColumnInfo(name = "id") // テーブルのカラム（列）名を指定
    private int id;

    @ColumnInfo(name = "image_res_id") // テーブルのカラム名を指定
    private int imageResId;

    @ColumnInfo(name = "is_unlocked") // テーブルのカラム名を指定
    private boolean isUnlocked;

    // コンストラクタ（データを生成する部分）
    public PuzzleData(int id, int imageResId, boolean isUnlocked) {
        this.id = id;
        this.imageResId = imageResId;
        this.isUnlocked = isUnlocked;
    }

    // --- ゲッター（データを取り出すためのメソッド） ---
    public int getId() {
        return id;
    }

    public int getImageResId() {
        return imageResId;
    }

    // isAcquired() から isUnlocked() に名前を合わせます
    public boolean isUnlocked() {
        return isUnlocked;
    }

    // --- セッター（データを後から変更するためのメソッド） ---
    public void setId(int id) {
        this.id = id;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public void setUnlocked(boolean unlocked) {
        isUnlocked = unlocked;
    }
}

