package com.example.piecehuntkoshi_ver1;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabase;

@Database(entities = {PuzzlePieceEntity.class}, version = 1)
public abstract class PuzzlePieceDatabase extends RoomDatabase{
    public abstract PuzzlePieceDao puzzlePieceDao();
}
