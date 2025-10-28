package com.example.piecehuntkoshi_ver1;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "puzzle_pieces",
        foreignKeys = @ForeignKey(entity = Puzzle.class,
                                  parentColumns = "id",
                                  childColumns = "puzzle_id",
                                  onDelete = ForeignKey.CASCADE),
        indices = {@Index(value = {"puzzle_id"})})
public class PuzzleData {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "puzzle_id") // Which puzzle this piece belongs to
    private int puzzleId;

    @ColumnInfo(name = "image_res_id")
    private int imageResId;

    @ColumnInfo(name = "is_unlocked")
    private boolean isUnlocked;

    // Constructor
    public PuzzleData(int puzzleId, int imageResId, boolean isUnlocked) {
        this.puzzleId = puzzleId;
        this.imageResId = imageResId;
        this.isUnlocked = isUnlocked;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getPuzzleId() {
        return puzzleId;
    }

    public int getImageResId() {
        return imageResId;
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setPuzzleId(int puzzleId) {
        this.puzzleId = puzzleId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public void setUnlocked(boolean unlocked) {
        isUnlocked = unlocked;
    }
}
