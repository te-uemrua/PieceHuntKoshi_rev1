package com.example.piecehuntkoshi_ver1;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PuzzleScreenActivity extends AppCompatActivity {

    // メンバー変数としてRecyclerViewとAdapterを宣言
    private RecyclerView puzzleRecyclerView;
    private PuzzleAdapter puzzleAdapter;
    private List<Piece> pieceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // このActivityで表示するレイアウトファイルを指定
        setContentView(R.layout.puzzle_screen);

        // 1. レイアウトファイルからRecyclerViewの部品を見つけてくる
        puzzleRecyclerView = findViewById(R.id.puzzleRecyclerView);

        // 2. 表示するためのピースのデータリストを作成する
        initializePieceList();

        // 3. アダプターを作成し、ピースのリストを渡す
        puzzleAdapter = new PuzzleAdapter(pieceList);

        // 4. RecyclerViewに「3列のグリッド表示」で表示するように設定する
        puzzleRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        // 5. RecyclerViewにアダプターを設定する（これで初めて画面に表示される）
        puzzleRecyclerView.setAdapter(puzzleAdapter);
    }

    /**
     * 9個のパズルピースのリストを初期化するメソッド
     */
    private void initializePieceList() {
        pieceList = new ArrayList<>();
        // ここで9個分のピースデータをリストに追加する
        // 今は仮に、最初の1つだけ取得済み、残りは未取得としてデータを準備
        pieceList.add(new Piece(R.drawable.piece_1, true)); // 1番目のピース（取得済み）
        pieceList.add(new Piece(R.drawable.piece_2, true)); // 2番目のピース（未取得）
        pieceList.add(new Piece(R.drawable.piece_3, true));
        pieceList.add(new Piece(R.drawable.piece_4, false));
        pieceList.add(new Piece(R.drawable.piece_5, false));
        pieceList.add(new Piece(R.drawable.piece_6, false));
        pieceList.add(new Piece(R.drawable.piece_7, false));
        pieceList.add(new Piece(R.drawable.piece_8, false));
        pieceList.add(new Piece(R.drawable.piece_9, false));
        // 注意： R.drawable.piece_1 や R.drawable.piece_2 などの画像ファイルを
        // あなたがプロジェクトの res/drawable フォルダに用意する必要があります。
        // もし画像がまだなければ、コンパイルエラーになります。
    }
}
