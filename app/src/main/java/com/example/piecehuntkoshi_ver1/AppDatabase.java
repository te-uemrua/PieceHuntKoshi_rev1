package com.example.piecehuntkoshi_ver1;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {LandmarkPuzzlePiece.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{
    public abstract PuzzlePieceDao puzzlePieceDao();
}
