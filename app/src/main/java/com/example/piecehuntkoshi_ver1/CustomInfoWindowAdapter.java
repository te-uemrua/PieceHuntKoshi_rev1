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
        TextView descriptionView = view.findViewById(R.id.info_window_description); // 説明TextViewを取得

        // データをセット
        titleView.setText(landmark.getName());
        descriptionView.setText(landmark.getDescription()); // 説明文をセット

        // 画像リソースIDが 0 (未設定) でなければ画像をセット
        if (landmark.getImageResourceId() != 0) {
            imageView.setImageResource(landmark.getImageResourceId());
            imageView.setVisibility(View.VISIBLE);
        } else {
            // 画像がなければ非表示
            imageView.setVisibility(View.GONE);
        }
    }

    @NonNull
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        // ★★★ こちらで mWindow を返す ★★★
        // これにより、デフォルトの白いフキダシ（枠）を使わず、
        // XMLで指定した @drawable/glass_card が背景として使われます。
        renderWindowText(marker, mWindow);
        return mWindow;
    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        // ★★★ こちらで null を返す ★★★
        // getInfoWindow で View を返した場合、こちらは呼び出されません。
        return null;
    }
}