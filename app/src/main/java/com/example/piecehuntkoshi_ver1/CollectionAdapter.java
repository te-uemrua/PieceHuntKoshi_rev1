package com.example.piecehuntkoshi_ver1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.PuzzleViewHolder> {

    private List<Puzzle> puzzleList;
    private final OnPuzzleClickListener listener;

    public interface OnPuzzleClickListener {
        void onPuzzleClick(Puzzle puzzle);
    }

    public CollectionAdapter(List<Puzzle> puzzleList, OnPuzzleClickListener listener) {
        this.puzzleList = puzzleList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PuzzleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.puzzle_thumbnail_item, parent, false);
        return new PuzzleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PuzzleViewHolder holder, int position) {
        Puzzle currentPuzzle = puzzleList.get(position);
        holder.bind(currentPuzzle, listener);
    }

    @Override
    public int getItemCount() {
        return puzzleList.size();
    }

    public static class PuzzleViewHolder extends RecyclerView.ViewHolder {
        public ImageView puzzleThumbnail;
        public TextView puzzleName;

        public PuzzleViewHolder(@NonNull View itemView) {
            super(itemView);
            puzzleThumbnail = itemView.findViewById(R.id.puzzle_thumbnail_image);
            puzzleName = itemView.findViewById(R.id.puzzle_thumbnail_name);
        }

        public void bind(final Puzzle puzzle, final OnPuzzleClickListener listener) {
            puzzleName.setText(puzzle.getName());
            
            if (puzzle.isCompleted()) {
                // If the puzzle is completed, show the actual thumbnail
                puzzleThumbnail.setImageResource(puzzle.getCompletedThumbnailResId());
                puzzleThumbnail.setAlpha(1.0f); // Make it fully visible
            } else {
                // If not completed, show a placeholder and make it look disabled
                puzzleThumbnail.setImageResource(puzzle.getThumbnailResId()); // A generic placeholder image
                puzzleThumbnail.setAlpha(0.5f); // Make it look greyed out
            }

            itemView.setOnClickListener(v -> listener.onPuzzleClick(puzzle));
        }
    }
}
