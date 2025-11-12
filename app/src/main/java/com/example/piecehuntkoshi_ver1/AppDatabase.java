package com.example.piecehuntkoshi_ver1;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {Puzzle.class, PuzzleData.class}, version = 8, exportSchema = false)
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
                            .addCallback(sRoomDatabaseCallback) // データベース作成時に初期データを投入する
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

                // --- 全てのパズルとピースの初期データをここで一度に作成 --- 

                // Puzzle 1: 熊本高専
                dao.insertPuzzle(new Puzzle("熊本高専", R.drawable.question, R.drawable.piece_1complete, false));
                List<PuzzleData> pieces1 = new ArrayList<>();
                int[] puzzle1Images = { 
                    R.drawable.piece_1, R.drawable.piece_2, R.drawable.piece_3, 
                    R.drawable.piece_4, R.drawable.piece_5, R.drawable.piece_6, 
                    R.drawable.piece_7, R.drawable.piece_8, R.drawable.piece_9 
                };
                for (int i = 0; i < 9; i++) {
                    pieces1.add(new PuzzleData(1, i, puzzle1Images[i], false));
                }
                dao.insertAllPieces(pieces1);

                // Puzzle 2: 教室
                dao.insertPuzzle(new Puzzle("教室", R.drawable.question, R.drawable.piece_2complete, false));
                List<PuzzleData> pieces2 = new ArrayList<>();
                int[] puzzle2Images = { 
                    R.drawable.piece_21, R.drawable.piece_22, R.drawable.piece_23, 
                    R.drawable.piece_24, R.drawable.piece_25, R.drawable.piece_26, 
                    R.drawable.piece_27, R.drawable.piece_28, R.drawable.piece_29 
                };
                for (int i = 0; i < 9; i++) {
                    pieces2.add(new PuzzleData(2, i, puzzle2Images[i], false));
                }
                dao.insertAllPieces(pieces2);

                // Puzzle 3: 大阪？
                dao.insertPuzzle(new Puzzle("大阪？", R.drawable.question, R.drawable.piece_3complete, false));
                List<PuzzleData> pieces3 = new ArrayList<>();
                int[] puzzle3Images = { 
                    R.drawable.piece_31, R.drawable.piece_32, R.drawable.piece_33, 
                    R.drawable.piece_34, R.drawable.piece_35, R.drawable.piece_36, 
                    R.drawable.piece_37, R.drawable.piece_38, R.drawable.piece_39 
                };
                for (int i = 0; i < 9; i++) {
                    pieces3.add(new PuzzleData(3, i, puzzle3Images[i], false));
                }
                dao.insertAllPieces(pieces3);
            });
        }
    };
}
