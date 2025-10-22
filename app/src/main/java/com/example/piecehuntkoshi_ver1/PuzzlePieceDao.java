package com.example.piecehuntkoshi_ver1;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PuzzlePieceDao {

    @Insert
    void insert(LandmarkPuzzlePiece landmarkPuzzlePiece);
    @Insert
    void insertAll(LandmarkPuzzlePiece... landmarkPuzzlePieces);

    @Delete
    void delete(LandmarkPuzzlePiece landmarkPuzzlePiece);
    @Delete
    void deleteAll(LandmarkPuzzlePiece... landmarkPuzzlePieces);

    @Update
    void updatePuzzlePiece(LandmarkPuzzlePiece... landmarkPuzzlePieces);

    @Query("SELECT * FROM LandmarkPuzzlePiece WHERE landmark_name = :landmark")
    List<LandmarkPuzzlePiece> findLandmark(String landmark);

}
