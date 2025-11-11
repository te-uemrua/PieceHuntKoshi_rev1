package com.example.piecehuntkoshi_ver1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
// import android.widget.ImageView; // ← 不要になるので削除
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;

    public CustomInfoWindowAdapter(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
    }

    private void renderWindowText(Marker marker, View view) {
        Landmark landmark = (Landmark) marker.getTag();
        if (landmark == null) {
            return;
        }

        TextView titleView = view.findViewById(R.id.info_window_title);
        TextView descriptionView = view.findViewById(R.id.info_window_description);

        titleView.setText(landmark.getName());
        descriptionView.setText(landmark.getDescription());

    }

    @NonNull
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        return null;
    }
}