package com.example.piecehuntkoshi_ver1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast; // Import the Toast class
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CollectionActivity extends AppCompatActivity implements CollectionAdapter.OnPuzzleClickListener {

    private RecyclerView collectionRecyclerView;
    private CollectionAdapter collectionAdapter;
    private TextView collectedPuzzlesCountText;
    private Button backToMapButton;

    private AppDatabase db;
    private ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        collectedPuzzlesCountText = findViewById(R.id.collected_pieces_count_text);
        backToMapButton = findViewById(R.id.back_to_map_from_collection_button);

        db = AppDatabase.getDatabase(getApplicationContext());

        setupRecyclerView();

        backToMapButton.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllPuzzles(); // Renamed from loadCompletedPuzzles
    }

    private void setupRecyclerView() {
        collectionRecyclerView = findViewById(R.id.collectionRecyclerView);
        collectionRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    }

    private void loadAllPuzzles() { // Renamed from loadCompletedPuzzles
        databaseExecutor.execute(() -> {
            if (db.puzzleDao().getPuzzleCount() == 0) {
                addDummyPuzzles();
            }

            // Fetch ALL puzzles from the database
            List<Puzzle> allPuzzles = db.puzzleDao().getAllPuzzles();

            runOnUiThread(() -> {
                collectionAdapter = new CollectionAdapter(allPuzzles, this);
                collectionRecyclerView.setAdapter(collectionAdapter);

                // You might want to update this text logic later
                long completedCount = allPuzzles.stream().filter(Puzzle::isCompleted).count();
                String countText = completedCount + " / " + allPuzzles.size() + " 個コンプリート";
                collectedPuzzlesCountText.setText(countText);
            });
        });
    }

    private void addDummyPuzzles() {
        // Add one completed and a few uncompleted puzzles for testing
        db.puzzleDao().insertPuzzle(new Puzzle("熊本県農業公園", R.drawable.ic_launcher_background, true));
        db.puzzleDao().insertPuzzle(new Puzzle("No.2", R.drawable.ic_launcher_foreground, false));
        db.puzzleDao().insertPuzzle(new Puzzle("No.3", R.drawable.ic_launcher_foreground, false));
    }

    @Override
    public void onPuzzleClick(Puzzle puzzle) {
        // Allow clicking only on completed puzzles
        if (puzzle.isCompleted()) {
            Intent intent = new Intent(this, PuzzleActivity.class);
            intent.putExtra("PUZZLE_ID", puzzle.getId());
            startActivity(intent);
        } else {
            Toast.makeText(this, "このパズルはまだ完成していません", Toast.LENGTH_SHORT).show();
        }
    }
}
