package com.example.piecehuntkoshi_ver1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PuzzleScreenActivity extends AppCompatActivity {

    // private RecyclerView puzzleRecyclerView;
    // private PuzzleAdapter puzzleAdapter;
    // private List<Piece> pieceList;
    // private TextView remainingPiecesText;
    // private Button backToMapButton;

    // PuzzleScreenActivity.java

// ... (他の部分は変更なし) ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle_screen);

        // ... (変数の宣言などは変更なし) ...
        RecyclerView puzzleRecyclerView = findViewById(R.id.puzzleRecyclerView);
        TextView remainingPiecesText = findViewById(R.id.remaining_pieces_text);
        Button backToMapButton = findViewById(R.id.back_to_map_button);

        List<Piece> pieceList = initializePieceList();
        PuzzleAdapter puzzleAdapter = new PuzzleAdapter(pieceList);
        puzzleRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        puzzleRecyclerView.setAdapter(puzzleAdapter);

        // 6. ★★★ パズルの状態をチェックして、メッセージを表示（strings.xml を使用） ★★★
        if (isPuzzleComplete(pieceList)) {
            // R.string.puzzle_complete を使って文字列を設定
            remainingPiecesText.setText(getString(R.string.puzzle_complete));
        } else {
            int remainingCount = getRemainingPieceCount(pieceList);
            // R.string.remaining_pieces_format をテンプレートとして使い、remainingCountを渡す
            String formattedText = getString(R.string.remaining_pieces_format, remainingCount);
            remainingPiecesText.setText(formattedText);
        }

        // 7. ★★★「マップに戻る」ボタンの処理を、より簡潔なラムダ式に修正 ★★★
        backToMapButton.setOnClickListener(v -> finish());
    }

// ... (他のメソッドは変更なし) ...

    /**
     * 9個のパズルピースのリストを初期化するメソッド
     * @return pieceList
     */
    private List<Piece> initializePieceList() { // ★戻り値の型をList<Piece>に変更
        List<Piece> localPieceList = new ArrayList<>();
        // ... (中身は同じ)
        localPieceList.add(new Piece(0, R.drawable.piece_1, true));
        localPieceList.add(new Piece(1, R.drawable.piece_2, true));
        localPieceList.add(new Piece(2, R.drawable.piece_3, true));
        localPieceList.add(new Piece(3, R.drawable.piece_4, true));
        localPieceList.add(new Piece(4, R.drawable.piece_5, true));
        localPieceList.add(new Piece(5, R.drawable.piece_6, true));
        localPieceList.add(new Piece(6, R.drawable.piece_7, true));
        localPieceList.add(new Piece(7, R.drawable.piece_8, true));
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
