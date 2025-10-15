package com.example.piecehuntkoshi_ver1;

import android.os.Bundle;
// import android.widget.Toast; // Toastはもう使わないので、この行は消してもOK
import android.widget.Button;
import android.widget.TextView; // ★★★ TextViewを使うためにimportを追加 ★★★import android.widget.Button;   // ★★★ Buttonを使うためにimportを追加 ★★★
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PuzzleScreenActivity extends AppCompatActivity {

    // メンバー変数
    private RecyclerView puzzleRecyclerView;
    private PuzzleAdapter puzzleAdapter;
    private List<Piece> pieceList;
    private TextView remainingPiecesText; // ★★★ ヘッダーのテキスト用変数を追加 ★★★
    private Button backToMapButton;       // ★★★ 戻るボタン用の変数を追加 ★★★

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle_screen);

        // 1. レイアウトファイルから各UI部品を見つけてくる
        puzzleRecyclerView = findViewById(R.id.puzzleRecyclerView);
        remainingPiecesText = findViewById(R.id.remaining_pieces_text); // ★★★ XMLのIDと紐付け ★★★
        backToMapButton = findViewById(R.id.back_to_map_button);        // ★★★ XMLのIDと紐付け ★★★

        // 2. 表示するためのピースのデータリストを作成する
        initializePieceList();

        // 3. アダプターを作成し、ピースのリストを渡す
        puzzleAdapter = new PuzzleAdapter(pieceList);

        // 4. RecyclerViewにレイアウトマネージャーを設定する
        puzzleRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        // 5. RecyclerViewにアダプターを設定する
        puzzleRecyclerView.setAdapter(puzzleAdapter);

        // 6. ★★★ パズルの状態をチェックして、ヘッダーのTextViewにメッセージを表示（Toastの代わりにsetText） ★★★
        if (isPuzzleComplete()) {
            // 完成していたらメッセージを表示
            remainingPiecesText.setText("パズル完成！");
        } else {
            // 未完成の場合、残り枚数を計算して表示
            int remainingCount = getRemainingPieceCount();
            remainingPiecesText.setText("あと" + remainingCount + "枚で完成！");
        }

        // 7. ★★★「マップに戻る」ボタンが押された時の処理を追加 ★★★
        backToMapButton.setOnClickListener(v -> {
            finish(); // このアクティビティを閉じて、前の画面（マップ画面）に戻る
        });
    }

    /**
     * 9個のパズルピースのリストを初期化するメソッド
     */
    private void initializePieceList() {
        pieceList = new ArrayList<>();

        // 新しいコンストラクタ new Piece(id, resourceId, isAcquired) に合わせて修正
        // （動作確認のために1つだけfalseにしています）
        pieceList.add(new Piece(0, R.drawable.piece_1, true));
        pieceList.add(new Piece(1, R.drawable.piece_2, true));
        pieceList.add(new Piece(2, R.drawable.piece_3, true));
        pieceList.add(new Piece(3, R.drawable.piece_4, true));
        pieceList.add(new Piece(4, R.drawable.piece_5, true));
        pieceList.add(new Piece(5, R.drawable.piece_6, true));
        pieceList.add(new Piece(6, R.drawable.piece_7, true));
        pieceList.add(new Piece(7, R.drawable.piece_8, true));
        pieceList.add(new Piece(8, R.drawable.piece_9, false));
    }

    /**
     * パズルが完成したかチェックするメソッド
     */
    private boolean isPuzzleComplete() {
        for (Piece piece : pieceList) {
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
    private int getRemainingPieceCount() {
        int remainingCount = 0;
        for (Piece piece : pieceList) {
            if (!piece.isAcquired()) {
                remainingCount++;
            }
        }
        return remainingCount;
    }
}
