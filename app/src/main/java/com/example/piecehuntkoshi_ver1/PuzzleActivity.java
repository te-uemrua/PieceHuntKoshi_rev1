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
import java.util.concurrent.Executors; // ★ 新しくインポート

public class PuzzleActivity extends AppCompatActivity {

    private RecyclerView puzzleRecyclerView;
    private PuzzleAdapter puzzleAdapter;
    private List<PuzzleData> pieceList = new ArrayList<>(); // ★ 初期化しておく
    private TextView remainingPiecesText;
    private Button backToMapButton;

    private AppDatabase db; // ★ データベースのインスタンスを保持する変数
    private ExecutorService databaseExecutor = Executors.newSingleThreadExecutor(); // ★ バックグラウンド処理用のExecutor

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle_screen);

        // --- UI部品の取得 ---
        puzzleRecyclerView = findViewById(R.id.puzzleRecyclerView);
        remainingPiecesText = findViewById(R.id.remaining_pieces_text);
        backToMapButton = findViewById(R.id.back_to_map_button);

        // --- RecyclerViewの初期設定 ---
        puzzleAdapter = new PuzzleAdapter(pieceList);
        puzzleRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        puzzleRecyclerView.setAdapter(puzzleAdapter);

        // ★ データベースのインスタンスを取得
        db = AppDatabase.getDatabase(getApplicationContext());

        // ★ データベースからピース情報を読み込む
        loadPiecesFromDatabase();

        // --- ボタンの処理 ---
        backToMapButton.setOnClickListener(v -> finish());
    }

    /**
     * ★★★ 新しい処理 ★★★
     * データベースからピースのリストを非同期で読み込む
     */
    private void loadPiecesFromDatabase() {
        databaseExecutor.execute(() -> {
            // バックグラウンドスレッドで実行される処理
            int pieceCount = db.puzzleDao().getPieceCount();
            if (pieceCount == 0) {
                // DBが空なら、初期データを投入
                db.puzzleDao().insertAll(initializePieceList());
            }

            // DBから全データを取得
            List<PuzzleData> piecesFromDb = db.puzzleDao().getAllPieces();

            // メインスレッド（UIスレッド）でUIを更新
            runOnUiThread(() -> {
                updatePuzzleView(piecesFromDb);
            });
        });
    }

    /**
     * ★★★ 新しい処理 ★★★
     * 取得したピースリストでUIを更新する
     * @param pieces データベースから取得したピースのリスト
     */
    private void updatePuzzleView(List<PuzzleData> pieces) {
        this.pieceList.clear();
        this.pieceList.addAll(pieces);
        puzzleAdapter.notifyDataSetChanged(); // RecyclerViewにデータの変更を通知

        if (isPuzzleComplete(this.pieceList)) {
            remainingPiecesText.setText(getString(R.string.puzzle_complete));
        } else {
            int remainingCount = getRemainingPieceCount(this.pieceList);

            String formattedText = getString(R.string.remaining_pieces_format, remainingCount);
            remainingPiecesText.setText(formattedText);
        }
    }


    /**
     * ★★★ 役割が変更 ★★★
     * 9個のパズルピースの「初期データ」のリストを作成するメソッド
     * @return 初期状態のピースリスト
     */
    private List<PuzzleData> initializePieceList() {
        List<PuzzleData> localPieceList = new ArrayList<>();
        // isUnlockedを全てfalse（未取得）にしておくのが一般的
        localPieceList.add(new PuzzleData(0, R.drawable.piece_1, false)); // piece_1は仮
        localPieceList.add(new PuzzleData(1, R.drawable.piece_2, false)); // piece_2は仮
        localPieceList.add(new PuzzleData(2, R.drawable.piece_3, false)); // etc...
        localPieceList.add(new PuzzleData(3, R.drawable.piece_4, false));
        localPieceList.add(new PuzzleData(4, R.drawable.piece_5, false));
        localPieceList.add(new PuzzleData(5, R.drawable.piece_6, false));
        localPieceList.add(new PuzzleData(6, R.drawable.piece_7, false));
        localPieceList.add(new PuzzleData(7, R.drawable.piece_8, false));
        localPieceList.add(new PuzzleData(8, R.drawable.piece_9, false));
        return localPieceList;
    }

    /**
     * パズルが完成したかチェックするメソッド（変更なし）
     */
    private boolean isPuzzleComplete(List<PuzzleData> pieces) {
        for (PuzzleData piece : pieces) {
            if (!piece.isUnlocked()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 残りの未取得ピースの数を数えるメソッド（変更なし）
     */
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
