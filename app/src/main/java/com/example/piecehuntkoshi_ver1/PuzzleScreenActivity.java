package com.example.piecehuntkoshi_ver1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PuzzleScreenActivity extends AppCompatActivity {

    // ★★★ メンバー変数をすべて削除 ★★★
    // private RecyclerView puzzleRecyclerView;
    // private PuzzleAdapter puzzleAdapter;
    // private List<Piece> pieceList;
    // private TextView remainingPiecesText;
    // private Button backToMapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle_screen);

        // ★★★ すべての変数をonCreateメソッド内のローカル変数として宣言 ★★★
        RecyclerView puzzleRecyclerView = findViewById(R.id.puzzleRecyclerView);
        TextView remainingPiecesText = findViewById(R.id.remaining_pieces_text);
        Button backToMapButton = findViewById(R.id.back_to_map_button);

        // 2. 表示するためのピースのデータリストを作成する
        List<Piece> pieceList = initializePieceList();

        // 3. アダプターを作成し、ピースのリストを渡す
        PuzzleAdapter puzzleAdapter = new PuzzleAdapter(pieceList);

        // 4. RecyclerViewにレイアウトマネージャーを設定する
        puzzleRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        // 5. RecyclerViewにアダプターを設定する
        puzzleRecyclerView.setAdapter(puzzleAdapter);

        // 6. パズルの状態をチェックして、ヘッダーのTextViewにメッセージを表示
        if (isPuzzleComplete(pieceList)) { // ★引数を渡す
            remainingPiecesText.setText("パズル完成！");
        } else {
            int remainingCount = getRemainingPieceCount(pieceList); // ★引数を渡す
            remainingPiecesText.setText("あと" + remainingCount + "枚で完成！");
        }

        // 7. 「マップに戻る」ボタンが押された時の処理を追加
        backToMapButton.setOnClickListener(v -> {
            finish();
        });
    }

    /**
     * 9個のパズルピースのリストを初期化するメソッド
     * @return pieceList
     */
    private List<Piece> initializePieceList() { // ★戻り値の型をList<Piece>に変更
        List<Piece> localPieceList = new ArrayList<>();
        // ... (中身は同じ)
        localPieceList.add(new Piece(0, R.drawable.piece_1, false));
        localPieceList.add(new Piece(1, R.drawable.piece_2, false));
        localPieceList.add(new Piece(2, R.drawable.piece_3, false));
        localPieceList.add(new Piece(3, R.drawable.piece_4, false));
        localPieceList.add(new Piece(4, R.drawable.piece_5, false));
        localPieceList.add(new Piece(5, R.drawable.piece_6, false));
        localPieceList.add(new Piece(6, R.drawable.piece_7, false));
        localPieceList.add(new Piece(7, R.drawable.piece_8, false));
        localPieceList.add(new Piece(8, R.drawable.piece_9, false));
        return localPieceList; // ★作成したリストを返す
    }

    /**
     * パズルが完成したかチェックするメソッド
     */
    private boolean isPuzzleComplete(List<Piece> pieces) { // ★引数でリストを受け取る
        for (Piece piece : pieces) {
            if (!piece.isAcquired()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 残りの未取得ピースの数を数えるメソッド
     * @return 残りのピース数
     */
    private int getRemainingPieceCount(List<Piece> pieces) { // ★引数でリストを受け取る
        int remainingCount = 0;
        for (Piece piece : pieces) {
            if (!piece.isAcquired()) {
                remainingCount++;
            }
        }
        return remainingCount;
    }
}
