package com.example.piecehuntkoshi_ver1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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
        // custom_info_window.xml のレイアウトを読み込む
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
    }

    private void renderWindowText(Marker marker, View view) {
        // マーカーにタグ付けされたLandmarkオブジェクトを取得
        Landmark landmark = (Landmark) marker.getTag();
        if (landmark == null) {
            return;
        }

        // Viewのコンポーネントを取得
        ImageView imageView = view.findViewById(R.id.info_window_image);
        TextView titleView = view.findViewById(R.id.info_window_title);
        TextView descriptionView = view.findViewById(R.id.info_window_description);

        // データをセット
        titleView.setText(landmark.getName());
        descriptionView.setText(landmark.getDescription());

        // 画像リソースIDが 0 (未設定) でなければ画像をセット
        if (landmark.getImageResourceId() != 0) {
            imageView.setImageResource(landmark.getImageResourceId());
            imageView.setVisibility(View.VISIBLE);
        } else {
            // 画像がなければ非表示
            imageView.setVisibility(View.GONE);
        }
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        // このメソッドは null を返します（ウィンドウ枠はデフォルトを使うため）
        return null;
    }

    @NonNull
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        // このメソッドでカスタムビュー（mWindow）を返します
        renderWindowText(marker, mWindow);
        return mWindow;
    }
}