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

    // Thumbnail for when the puzzle is NOT completed (e.g., a question mark)
    @ColumnInfo(name = "thumbnail_res_id")
    private int thumbnailResId;

    // Thumbnail for when the puzzle IS completed (the actual puzzle image)
    @ColumnInfo(name = "completed_thumbnail_res_id")
    private int completedThumbnailResId;

    @ColumnInfo(name = "is_completed")
    private boolean isCompleted;

    // Constructor (now with 4 arguments)
    public Puzzle(String name, int thumbnailResId, int completedThumbnailResId, boolean isCompleted) {
        this.name = name;
        this.thumbnailResId = thumbnailResId;
        this.completedThumbnailResId = completedThumbnailResId;
        this.isCompleted = isCompleted;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getThumbnailResId() {
        return thumbnailResId;
    }

    public int getCompletedThumbnailResId() {
        return completedThumbnailResId;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setThumbnailResId(int thumbnailResId) {
        this.thumbnailResId = thumbnailResId;
    }

    public void setCompletedThumbnailResId(int completedThumbnailResId) {
        this.completedThumbnailResId = completedThumbnailResId;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
