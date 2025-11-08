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

                dao.insertPuzzle(new Puzzle("No.1", R.drawable.question, true));

                dao.insertPuzzle(new Puzzle("No.2", R.drawable.question, false));

                dao.insertPuzzle(new Puzzle("No.3", R.drawable.question, false));
            });
        }
    };
}
