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

    @Override
    public void onBindViewHolder(@NonNull PieceViewHolder holder, int position){
        PuzzleData currentPiece = pieceList.get(position);
        if(currentPiece.isUnlocked()){
            holder.pieceImageView.setImageResource(currentPiece.getImageResId());
        }else{
            holder.pieceImageView.setImageResource(R.drawable.mask_piece_shape);
        }
    }

    @Override
    public int getItemCount(){
        return pieceList.size();
    }
}
