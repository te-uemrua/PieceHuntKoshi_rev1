package com.example.piecehuntkoshi_ver1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Locale;

public class LandmarkAdapter extends RecyclerView.Adapter<LandmarkAdapter.LandmarkViewHolder> {

    private List<Landmark> landmarkList;

    public LandmarkAdapter(List<Landmark> landmarkList) {
        this.landmarkList = landmarkList;
    }

    @NonNull
    @Override
    public LandmarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_landmark, parent, false);
        return new LandmarkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LandmarkViewHolder holder, int position) {
        Landmark landmark = landmarkList.get(position);
        holder.nameTextView.setText(landmark.getName());

        // 距離が計算されていれば表示
        if (landmark.getDistance() >= 0) {
            holder.distanceTextView.setText(String.format(Locale.JAPAN, "目的地までの距離: %.0f m", landmark.getDistance()));
        } else {
            holder.distanceTextView.setText("ここまでの距離: 計測中...");
        }
    }

    @Override
    public int getItemCount() {
        return landmarkList.size();
    }

    static class LandmarkViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView distanceTextView;

        public LandmarkViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.landmark_name);
            distanceTextView = itemView.findViewById(R.id.landmark_distance);
        }
    }
}