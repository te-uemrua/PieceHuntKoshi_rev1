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
    void insertAll(PuzzlePieceEntity... puzzlePieceEntities);

    @Delete
    void deleteAll(PuzzlePieceEntity... puzzlePieceEntities);

    @Update
    void updatePuzzlePiece(PuzzlePieceEntity... puzzlePieceEntities);

}
