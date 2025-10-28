package com.example.piecehuntkoshi_ver1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PuzzleActivity extends AppCompatActivity {

    private RecyclerView puzzleRecyclerView;
    private PuzzleAdapter puzzleAdapter;
    private List<PuzzleData> pieceList = new ArrayList<>();
    private TextView remainingPiecesText;
    private Button backToMapButton;

    private AppDatabase db;
    private ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();

    private int currentPuzzleId = -1; // Variable to hold the passed puzzle ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle_screen);

        // Get the puzzleId from the Intent
        currentPuzzleId = getIntent().getIntExtra("PUZZLE_ID", -1);
        if (currentPuzzleId == -1) {
            // If no ID is passed, finish the activity as it cannot display anything
            finish();
            return;
        }

        puzzleRecyclerView = findViewById(R.id.puzzleRecyclerView);
        remainingPiecesText = findViewById(R.id.remaining_pieces_text);
        backToMapButton = findViewById(R.id.back_to_map_button);

        puzzleAdapter = new PuzzleAdapter(pieceList);
        puzzleRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        puzzleRecyclerView.setAdapter(puzzleAdapter);

        db = AppDatabase.getDatabase(getApplicationContext());

        loadPiecesFromDatabase();

        backToMapButton.setOnClickListener(v -> finish());
    }

    private void loadPiecesFromDatabase() {
        databaseExecutor.execute(() -> {
            // Load pieces for the specific puzzle ID
            List<PuzzleData> piecesFromDb = db.puzzleDao().getPiecesForPuzzle(currentPuzzleId);

            // If the puzzle has no pieces, initialize them (optional, depends on your logic)
            if (piecesFromDb.isEmpty()) {
                piecesFromDb = initializePieceListForPuzzle(currentPuzzleId);
                db.puzzleDao().insertAllPieces(piecesFromDb);
            }

            List<PuzzleData> finalPiecesFromDb = piecesFromDb;
            runOnUiThread(() -> {
                updatePuzzleView(finalPiecesFromDb);
            });
        });
    }

    private void updatePuzzleView(List<PuzzleData> pieces) {
        this.pieceList.clear();
        this.pieceList.addAll(pieces);
        puzzleAdapter.notifyDataSetChanged();

        if (isPuzzleComplete(this.pieceList)) {
            remainingPiecesText.setText(getString(R.string.puzzle_complete));
        } else {
            int remainingCount = getRemainingPieceCount(this.pieceList);
            String formattedText = getString(R.string.remaining_pieces_format, remainingCount);
            remainingPiecesText.setText(formattedText);
        }
    }

    // This method now initializes pieces for a specific puzzle
    private List<PuzzleData> initializePieceListForPuzzle(int puzzleId) {
        List<PuzzleData> localPieceList = new ArrayList<>();
        // This logic needs to be adapted based on your puzzle designs
        // For now, it just creates 9 generic pieces for the given puzzleId
        localPieceList.add(new PuzzleData(puzzleId, R.drawable.piece_1, false));
        localPieceList.add(new PuzzleData(puzzleId, R.drawable.piece_2, false));
        localPieceList.add(new PuzzleData(puzzleId, R.drawable.piece_3, false));
        localPieceList.add(new PuzzleData(puzzleId, R.drawable.piece_4, false));
        localPieceList.add(new PuzzleData(puzzleId, R.drawable.piece_5, false));
        localPieceList.add(new PuzzleData(puzzleId, R.drawable.piece_6, false));
        localPieceList.add(new PuzzleData(puzzleId, R.drawable.piece_7, false));
        localPieceList.add(new PuzzleData(puzzleId, R.drawable.piece_8, false));
        localPieceList.add(new PuzzleData(puzzleId, R.drawable.piece_9, false));
        return localPieceList;
    }

    private boolean isPuzzleComplete(List<PuzzleData> pieces) {
        for (PuzzleData piece : pieces) {
            if (!piece.isUnlocked()) {
                return false;
            }
        }
        return true;
    }

    private int getRemainingPieceCount(List<PuzzleData> pieces) {
        int remainingCount = 0;
        for (PuzzleData piece : pieces) {
            if (!piece.isUnlocked()) {
                remainingCount++;
            }
        }
        return remainingCount;
    }
}
