package com.example.piecehuntkoshi_ver1;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "puzzles")
public class Puzzle {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "thumbnail_res_id")
    private int thumbnailResId;

    @ColumnInfo(name = "is_completed")
    private boolean isCompleted;

    public Puzzle(String name, int thumbnailResId, boolean isCompleted) {
        this.name = name;
        this.thumbnailResId = thumbnailResId;
        this.isCompleted = isCompleted;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getThumbnailResId() {
        return thumbnailResId;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setThumbnailResId(int thumbnailResId) {
        this.thumbnailResId = thumbnailResId;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
