package com.example.piecehuntkoshi_ver1; // あなたのパッケージ名に合わせてください

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * 地図を表示するためのメイン画面 (Activity)
 * OnMapReadyCallbackインターフェースを実装(implements)するのが必須
 */
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap; // 表示するGoogleMapオブジェクトを保持する変数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // activity_main.xmlを画面として設定
        setContentView(R.layout.activity_main);

        // activity_main.xmlの中から、地図を表示する部品(Fragment)を探してくる
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        // 地図の準備が非同期で完了したら、onMapReadyメソッドを呼び出すようにリクエスト
        mapFragment.getMapAsync(this);
    }

    /**
     * Google Mapの準備が完了したときに、自動的に呼び出されるメソッド
     * @param googleMap 準備が完了したGoogleMapオブジェクト
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // --- ここから地図に対する操作を記述 ---

        // 1. とりあえずの表示位置として「熊本県農業カントリーパーク」の緯度経度を指定
        LatLng koshiCityHall = new LatLng(32.8900575, 130.7595619);

        // 2. その位置にマーカー（ピン）を立てる
        mMap.addMarker(new MarkerOptions().position(koshiCityHall).title("熊本県農業カントリーパーク"));

        // 3. 指定した座標にカメラを移動させる（ズームレベルは15）
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(koshiCityHall, 14f));

        CircleOptions circleOptions = new CircleOptions()
                .center(koshiCityHall)  // 円の中心座標
                .radius(500)           // 半径 (メートル単位)
                .strokeColor(Color.RED) // 線の色 (赤)
                .strokeWidth(5f)        // 線の太さ
                .fillColor(0x55ff0000); // 塗りつぶしの色 (半透明の赤)

        // 2. 地図に円を追加する
        mMap.addCircle(circleOptions);

    }
}