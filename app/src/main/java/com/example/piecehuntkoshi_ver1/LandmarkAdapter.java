package com.example.piecehuntkoshi_ver1;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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
        if (landmark.getDistance() >= 0) {
            holder.distanceTextView.setText(String.format(Locale.JAPAN, "目的地までの距離: %.0f m", landmark.getDistance()));
        } else {
            holder.distanceTextView.setText("ここまでの距離: 計測中...");
        }

        if (landmark.isOnCooldown()) {
            long remainingMillis = (landmark.getLastAcquiredTimestamp() + (24 * 60 * 60 * 1000)) - System.currentTimeMillis();

            long hours = TimeUnit.MILLISECONDS.toHours(remainingMillis);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(remainingMillis) % 60;

            String cooldownText = String.format(Locale.JAPAN, "クールタイム中: あと %d時間%d分", hours, minutes);

            holder.cooldownTextView.setText(cooldownText);
            holder.cooldownTextView.setVisibility(View.VISIBLE);

            holder.distanceTextView.setTextColor(Color.GRAY);

        } else {
            holder.cooldownTextView.setVisibility(View.GONE);
            holder.distanceTextView.setTextColor(holder.distanceTextView.getContext().getColor(R.color.origin_yellow));
        }
    }

    @Override
    public int getItemCount() {
        return landmarkList.size();
    }

    static class LandmarkViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView distanceTextView;
        TextView cooldownTextView;

        public LandmarkViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.landmark_name);
            distanceTextView = itemView.findViewById(R.id.landmark_distance);
            cooldownTextView = itemView.findViewById(R.id.landmark_cooldown);
        }
    }
}