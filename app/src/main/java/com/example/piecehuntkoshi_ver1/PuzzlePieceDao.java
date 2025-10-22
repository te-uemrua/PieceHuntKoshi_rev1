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
    void insert(PuzzlePieceEntity puzzlePieceEntity);
    @Insert
    void insertAll(PuzzlePieceEntity... puzzlePieceEntities);

    @Delete
    void delete(PuzzlePieceEntity puzzlePieceEntity);
    @Delete
    void deleteAll(PuzzlePieceEntity... puzzlePieceEntities);

    @Update
    void updatePuzzlePiece(PuzzlePieceEntity... puzzlePieceEntities);

    @Query("SELECT * FROM puzzlePieceEntity WHERE landmark_name = :landmark")
    List<PuzzlePieceEntity> findLandmark(String landmark);

}
