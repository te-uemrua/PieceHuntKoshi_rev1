package com.example.piecehuntkoshi_ver1;

import android.content.Context;
import androidx.room.Room;

public class PuzzlePieceDatabaseSingleton {
    private static PuzzlePieceDatabase instance = null;

    public static PuzzlePieceDatabase getInstance(Context context){
        if (instance != null){
            return instance;
        }

        instance = Room.databaseBuilder(context, PuzzlePieceDatabase.class, "database-name").build();
        return instance;
    }
}
