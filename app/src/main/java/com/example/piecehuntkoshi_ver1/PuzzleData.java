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

    @ColumnInfo(name = "puzzle_id")
    private int puzzleId;

    @ColumnInfo(name = "piece_index") // The position of the piece in the puzzle (0-8)
    private int pieceIndex;

    @ColumnInfo(name = "image_res_id")
    private int imageResId;

    @ColumnInfo(name = "is_unlocked")
    private boolean isUnlocked;

    // Constructor
    public PuzzleData(int puzzleId, int pieceIndex, int imageResId, boolean isUnlocked) {
        this.puzzleId = puzzleId;
        this.pieceIndex = pieceIndex;
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

    public int getPieceIndex() {
        return pieceIndex;
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

    public void setPieceIndex(int pieceIndex) {
        this.pieceIndex = pieceIndex;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public void setUnlocked(boolean unlocked) {
        isUnlocked = unlocked;
    }
}
