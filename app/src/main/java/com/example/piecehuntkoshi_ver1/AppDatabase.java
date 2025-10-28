package com.example.piecehuntkoshi_ver1;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Puzzle.class, PuzzleData.class}, version = 2, exportSchema = false) // Add Puzzle.class and increment version
public abstract class AppDatabase extends RoomDatabase {

    public abstract PuzzleDao puzzleDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "puzzle_database")
                            .fallbackToDestructiveMigration() // Add this to handle migration by recreating the DB
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
