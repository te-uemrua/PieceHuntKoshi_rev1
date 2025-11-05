package com.example.piecehuntkoshi_ver1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class PuzzleAdapter extends RecyclerView.Adapter<PuzzleAdapter.PieceViewHolder>{

    private List<PuzzleData> pieceList;
    //コンストラクタ
    public PuzzleAdapter(List<PuzzleData> pieceList){
        this.pieceList = pieceList;
    }

    //ViewHolder
    public static class PieceViewHolder extends RecyclerView.ViewHolder{
        public ImageView pieceImageView;

        public PieceViewHolder(@NonNull View itemView){
            super(itemView);
            pieceImageView = itemView.findViewById(R.id.pieceImageView);
        }
    }

    @NonNull
    @Override
    public PieceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.piece_item, parent, false);
        return new PieceViewHolder(view);
    }

// PuzzleAdapter.java の onBindViewHolder メソッド

    @Override
    public void onBindViewHolder(@NonNull PieceViewHolder holder, int position) {
        PuzzleData currentPiece = pieceList.get(position);
        ImageView pieceImageView = holder.pieceImageView;

        // まず、どんな場合でも共通のピース画像（枠）を設定する
        // これにより、SquareImageViewは常に正方形を維持しようと働く
        pieceImageView.setImageResource(currentPiece.getImageResId());

        if (currentPiece.isUnlocked()) {
            // ★ 取得済みの場合
            // 前景のピース画像はそのまま活かし、背景は透明にする
            pieceImageView.setBackground(null);
            // 画像のアルファ値（不透明度）を100%（不透明）にする
            pieceImageView.setImageAlpha(255);

        } else {
            // ★ 未取得の場合
            // 前景のピース画像を半透明（ほとんど見えない状態）にする
            pieceImageView.setImageAlpha(30); // 0だと完全に消えてしまうことがあるため、少しだけ見えるようにする
            // 背景に「？」アイコンを設定する
            pieceImageView.setBackgroundResource(android.R.drawable.ic_menu_help);
        }
    }


    @Override
    public int getItemCount(){
        return pieceList.size();
    }
}
