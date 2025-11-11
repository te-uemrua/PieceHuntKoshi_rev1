package com.example.piecehuntkoshi_ver1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

    // (Existing variables)
    private RecyclerView puzzleRecyclerView;
    private PuzzleAdapter puzzleAdapter;
    private List<PuzzleData> pieceList = new ArrayList<>();
    private TextView remainingPiecesText;
    private Button backToMapButton;
    private AppDatabase db;
    private ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();
    private int currentPuzzleId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle_screen);

        currentPuzzleId = getIntent().getIntExtra("PUZZLE_ID", -1);
        if (currentPuzzleId == -1) {
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

        backToMapButton.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPiecesFromDatabase();
    }

    private void loadPiecesFromDatabase() {
        databaseExecutor.execute(() -> {
            List<PuzzleData> piecesFromDb = db.puzzleDao().getPiecesForPuzzle(currentPuzzleId);
            
            runOnUiThread(() -> {
                updatePuzzleView(piecesFromDb);
            });
        });
    }

    // Simplified the view update logic
    private void updatePuzzleView(List<PuzzleData> pieces) {
        if (pieces == null) {
            pieces = new ArrayList<>();
        }

        this.pieceList.clear();
        this.pieceList.addAll(pieces);
        puzzleAdapter.notifyDataSetChanged();

        int remainingCount = getRemainingPieceCount(this.pieceList);
        if (remainingCount == 0 && !this.pieceList.isEmpty()) {
             remainingPiecesText.setText(getString(R.string.puzzle_complete));
        } else {
             String formattedText = getString(R.string.remaining_pieces_format, remainingCount);
             remainingPiecesText.setText(formattedText);
        }
    }

    private boolean isPuzzleComplete(List<PuzzleData> pieces) {
        if (pieces == null || pieces.isEmpty()) return false;
        for (PuzzleData piece : pieces) {
            if (!piece.isUnlocked()) {
                return false;
            }
        }
        return true;
    }

    private int getRemainingPieceCount(List<PuzzleData> pieces) {
        if (pieces == null || pieces.isEmpty()) return 9; // Show 9 if list is empty
        int remainingCount = 0;
        for (PuzzleData piece : pieces) {
            if (!piece.isUnlocked()) {
                remainingCount++;
            }
        }
        return remainingCount;
    }
}
