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

    public PuzzleAdapter(List<PuzzleData> pieceList){
        this.pieceList = pieceList;
    }

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
    public void onBindViewHolder(@NonNull PieceViewHolder holder, int position) {
        PuzzleData currentPiece = pieceList.get(position);
        ImageView pieceImageView = holder.pieceImageView;

        pieceImageView.setImageResource(currentPiece.getImageResId());

        if (currentPiece.isUnlocked()) {
            pieceImageView.setBackground(null);
            pieceImageView.setImageAlpha(255);

        } else {
            pieceImageView.setImageAlpha(30);
            pieceImageView.setBackgroundResource(android.R.drawable.ic_menu_help);
        }
    }


    @Override
    public int getItemCount(){
        return pieceList.size();
    }
}
