package com.example.piecehuntkoshi_ver1;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executors;

@Database(entities = {Puzzle.class, PuzzleData.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PuzzleDao puzzleDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "puzzle_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            
            Executors.newSingleThreadExecutor().execute(() -> {
                PuzzleDao dao = INSTANCE.puzzleDao();

                // --- Create Initial Puzzles ONLY ---
                // The pieces will be created by PuzzleActivity when a puzzle is opened for the first time.

                // Puzzle 1 (Completed)
                dao.insertPuzzle(new Puzzle("熊本県農業公園", R.drawable.ic_launcher_background, true));

                // Puzzle 2 (Not Completed)
                dao.insertPuzzle(new Puzzle("No.2", R.drawable.ic_launcher_foreground, false));

                // Puzzle 3 (Not Completed)
                dao.insertPuzzle(new Puzzle("No.3", R.drawable.ic_launcher_foreground, false));
            });
        }
    };
}
