package com.example.piecehuntkoshi_ver1; // あなたのパッケージ名に合わせてください

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

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

        //「ランドマーク」の緯度経度を指定
        LatLng kumamotoKosen = new LatLng(32.876637,130.74851);
        LatLng countrypark = new LatLng(32.8900575, 130.7595619);
        LatLng takabajouatopark = new LatLng(32.89896389,130.79429999);
        LatLng ambkumamoto = new LatLng(32.880783,130.785207);
        LatLng koshigijukuato = new LatLng(32.9163671,130.7458907);

        // 2.ランドマークにマーカーを立てる
        mMap.addMarker(new MarkerOptions().position(kumamotoKosen).title("現在地 (熊本高専)"));
        mMap.addMarker(new MarkerOptions().position(countrypark).title("熊本県農業カントリーパーク"));
        mMap.addMarker(new MarkerOptions().position(takabajouatopark).title("竹迫城跡公園"));
        mMap.addMarker(new MarkerOptions().position(ambkumamoto).title("アンビー熊本"));
        mMap.addMarker(new MarkerOptions().position(koshigijukuato).title("合志義塾跡"));



        // 3. 指定した座標にカメラを移動させる（ズームレベルは15）
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kumamotoKosen, 14f));


        CircleOptions circleOptions1 = new CircleOptions()
                .center(countrypark)  // 円の中心座標
                .radius(500)           // 半径 (メートル単位)
                .strokeColor(Color.RED) // 線の色 (赤)
                .strokeWidth(5f)        // 線の太さ
                .fillColor(0x55ff0000); // 塗りつぶしの色 (半透明の赤)

        CircleOptions circleOptions2 = new CircleOptions()
                .center(takabajouatopark)  // 円の中心座標
                .radius(200)           // 半径 (メートル単位)
                .strokeColor(Color.RED) // 線の色 (赤)
                .strokeWidth(5f)        // 線の太さ
                .fillColor(0x55ff0000); // 塗りつぶしの色 (半透明の赤)

        CircleOptions circleOptions3 = new CircleOptions()
                .center(ambkumamoto)  // 円の中心座標
                .radius(100)           // 半径 (メートル単位)
                .strokeColor(Color.RED) // 線の色 (赤)
                .strokeWidth(5f)        // 線の太さ
                .fillColor(0x55ff0000); // 塗りつぶしの色 (半透明の赤)

        CircleOptions circleOptions4 = new CircleOptions()
                .center(koshigijukuato)  // 円の中心座標
                .radius(100)           // 半径 (メートル単位)
                .strokeColor(Color.RED) // 線の色 (赤)
                .strokeWidth(5f)        // 線の太さ
                .fillColor(0x55ff0000); // 塗りつぶしの色 (半透明の赤)

        // 2. 地図に円を追加する
        mMap.addCircle(circleOptions1);
        mMap.addCircle(circleOptions2);
        mMap.addCircle(circleOptions3);
        mMap.addCircle(circleOptions4);

        LatLng southWest = new LatLng(32.84, 130.72); // 合志市の南西の角 (おおよその座標)
        LatLng northEast = new LatLng(32.93, 130.82); // 合志市の北東の角 (おおよその座標)
        LatLngBounds koshiBounds = new LatLngBounds(southWest, northEast);

        // 2. 地図にスクロール範囲を設定する
        mMap.setLatLngBoundsForCameraTarget(koshiBounds);

        // 3. これ以上ズームアウトできないように、最小ズームレベルを設定する (推奨)
        mMap.setMinZoomPreference(12.0f);

        PolygonOptions koshiBorderMoreDetailed = new PolygonOptions()
                .add(new LatLng(32.9298, 130.7655))
                .add(new LatLng(32.9285, 130.7760))
                .add(new LatLng(32.9221, 130.7831))
                .add(new LatLng(32.9174, 130.7953))
                .add(new LatLng(32.9125, 130.8093))
                .add(new LatLng(32.9034, 130.8160))
                .add(new LatLng(32.8943, 130.8183))
                .add(new LatLng(32.8833, 130.8195))
                .add(new LatLng(32.8687, 130.8203))
                .add(new LatLng(32.8542, 130.8143))
                .add(new LatLng(32.8480, 130.8050))
                .add(new LatLng(32.8412, 130.7951))
                .add(new LatLng(32.8368, 130.7752))
                .add(new LatLng(32.8391, 130.7600))
                .add(new LatLng(32.8446, 130.7515))
                .add(new LatLng(32.8421, 130.7401))
                .add(new LatLng(32.8448, 130.7303))
                .add(new LatLng(32.8551, 130.7265))
                .add(new LatLng(32.8633, 130.7248))
                .add(new LatLng(32.8805, 130.7219))
                .add(new LatLng(32.8953, 130.7314))
                .add(new LatLng(32.9051, 130.7345))
                .add(new LatLng(32.9108, 130.7388))
                .add(new LatLng(32.9189, 130.7495))
                .add(new LatLng(32.9255, 130.7578))
                .add(new LatLng(32.9298, 130.7655)); // 始点に戻り、多角形を閉じる

        // 2. 境界線の見た目を設定
        koshiBorderMoreDetailed.strokeColor(Color.argb(220, 255, 127, 62));   // 線の色を少し透明なオレンジに (ご指定のパレットより)
        koshiBorderMoreDetailed.strokeWidth(12f);                                // 線の太さを12に
        koshiBorderMoreDetailed.fillColor(Color.TRANSPARENT);                   // 塗りつぶしを透明に設定

        // 3. 地図にポリゴンを追加して境界線を描画
        mMap.addPolygon(koshiBorderMoreDetailed);

    }
}