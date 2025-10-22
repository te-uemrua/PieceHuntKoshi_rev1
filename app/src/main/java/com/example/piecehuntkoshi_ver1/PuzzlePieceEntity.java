package com.example.piecehuntkoshi_ver1;

import androidx.room.Entity;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

@Entity
public class PuzzlePieceEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "landmark_name")
    private String landmarkName;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "puzzlePiece_number")
    private int puzzlePieceNumber;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLandmarkName() {
        return landmarkName;
    }

    public void setLandmarkName(String landmarkName) {
        this.landmarkName = landmarkName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPuzzlePieceNumber(){
        return puzzlePieceNumber;
    }

    public void setPuzzlePieceNumber(int puzzlePieceNumber){
        this.puzzlePieceNumber = puzzlePieceNumber;
    }
}