package com.example.piecehuntkoshi_ver1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    // 権限リクエストの識別コード (任意の数字でOK)
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private GoogleMap mMap;
    private Button getPieceButton;
    private Button collectionButton; // ★★★ コンフリクト解消：collectionButtonの変数を追加 ★★★

    private FusedLocationProviderClient fusedLocationClient;
    private Handler locationHandler = new Handler(Looper.getMainLooper());
    private Runnable locationCheckRunnable;
    private Map<LatLng, Float> landmarks = new HashMap<>();
    private boolean isInLandmarkArea = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // --- RENTOさんの変更内容 ---
        getPieceButton = findViewById(R.id.get_piece_button);
        getPieceButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, shake_phone.class);
            startActivity(intent);
        });

        // ★★★ コンフリクト解消：もう一人のメンバーの変更内容を取り込む ★★★
        collectionButton = findViewById(R.id.collection_button);
        collectionButton.setOnClickListener(v -> {
            // 注意：遷移先のActivity名が不明なため、仮で `puzzle_screen.class` にしています。
            // 実際のクラス名（例: PuzzleScreenActivity.class）に修正してください。
            Intent intent = new Intent(MainActivity.this, PuzzleScreenActivity.class);
            startActivity(intent);
        });
        // ★★★ ここまで ★★★

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationChecks();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationChecks();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            enableMyLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }

        setupMapDrawings();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
            } else {
                Toast.makeText(this, "位置情報の許可がないため、現在地を表示できません", Toast.LENGTH_LONG).show();
                LatLng kumamotoKosen = new LatLng(32.876637, 130.74851);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kumamotoKosen, 14f));
            }
        }
    }

    private void enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 14f));
                } else {
                    Toast.makeText(this, "現在地が取得できませんでした。位置情報設定を確認してください。", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setupMapDrawings() {
        LatLng countrypark = new LatLng(32.8900575, 130.7595619);
        LatLng takabajouatopark = new LatLng(32.89896389, 130.79429999);
        LatLng ambkumamoto = new LatLng(32.880783, 130.785207);
        LatLng koshigijukuato = new LatLng(32.9163671, 130.7458907);

        landmarks.put(countrypark, 500f);
        landmarks.put(takabajouatopark, 200f);
        landmarks.put(ambkumamoto, 100f);
        landmarks.put(koshigijukuato, 100f);

        mMap.addMarker(new MarkerOptions().position(countrypark).title("熊本県農業カントリーパーク"));
        mMap.addMarker(new MarkerOptions().position(takabajouatopark).title("竹迫城跡公園"));
        mMap.addMarker(new MarkerOptions().position(ambkumamoto).title("アンビー熊本"));
        mMap.addMarker(new MarkerOptions().position(koshigijukuato).title("合志義塾跡"));

        mMap.addCircle(new CircleOptions().center(countrypark).radius(500).strokeColor(Color.RED).strokeWidth(5f).fillColor(0x55ff0000));
        mMap.addCircle(new CircleOptions().center(takabajouatopark).radius(200).strokeColor(Color.RED).strokeWidth(5f).fillColor(0x55ff0000));
        mMap.addCircle(new CircleOptions().center(ambkumamoto).radius(100).strokeColor(Color.RED).strokeWidth(5f).fillColor(0x55ff0000));
        mMap.addCircle(new CircleOptions().center(koshigijukuato).radius(100).strokeColor(Color.RED).strokeWidth(5f).fillColor(0x55ff0000));

        LatLng southWest = new LatLng(32.84, 130.72);
        LatLng northEast = new LatLng(32.93, 130.82);
        LatLngBounds koshiBounds = new LatLngBounds(southWest, northEast);
        mMap.setLatLngBoundsForCameraTarget(koshiBounds);
        mMap.setMinZoomPreference(12.0f);

        PolygonOptions koshiBorderMoreDetailed = new PolygonOptions()
                .add(new LatLng(32.9298, 130.7655)).add(new LatLng(32.9285, 130.7760))
                .add(new LatLng(32.9221, 130.7831)).add(new LatLng(32.9174, 130.7953))
                .add(new LatLng(32.9125, 130.8093)).add(new LatLng(32.9034, 130.8160))
                .add(new LatLng(32.8943, 130.8183)).add(new LatLng(32.8833, 130.8195))
                .add(new LatLng(32.8687, 130.8203)).add(new LatLng(32.8542, 130.8143))
                .add(new LatLng(32.8480, 130.8050)).add(new LatLng(32.8412, 130.7951))
                .add(new LatLng(32.8368, 130.7752)).add(new LatLng(32.8391, 130.7600))
                .add(new LatLng(32.8446, 130.7515)).add(new LatLng(32.8421, 130.7401))
                .add(new LatLng(32.8448, 130.7303)).add(new LatLng(32.8551, 130.7265))
                .add(new LatLng(32.8633, 130.7248)).add(new LatLng(32.8805, 130.7219))
                .add(new LatLng(32.8953, 130.7314)).add(new LatLng(32.9051, 130.7345))
                .add(new LatLng(32.9108, 130.7388)).add(new LatLng(32.9189, 130.7495))
                .add(new LatLng(32.9255, 130.7578)).add(new LatLng(32.9298, 130.7655));
        koshiBorderMoreDetailed.strokeColor(Color.argb(220, 255, 127, 62));
        koshiBorderMoreDetailed.strokeWidth(12f);
        koshiBorderMoreDetailed.fillColor(Color.TRANSPARENT);
        mMap.addPolygon(koshiBorderMoreDetailed);
    }

    private void startLocationChecks() {
        locationCheckRunnable = new Runnable() {
            @Override
            public void run() {
                checkDistanceToLandmarks();
                locationHandler.postDelayed(this, 5000);
            }
        };
        locationHandler.post(locationCheckRunnable);
    }

    private void stopLocationChecks() {
        if (locationCheckRunnable != null) {
            locationHandler.removeCallbacks(locationCheckRunnable);
        }
    }

    private void checkDistanceToLandmarks() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location == null) return;

            boolean currentlyInArea = false;
            for (Map.Entry<LatLng, Float> entry : landmarks.entrySet()) {
                LatLng landmarkLocation = entry.getKey();
                float radius = entry.getValue();

                float[] results = new float[1];
                Location.distanceBetween(location.getLatitude(), location.getLongitude(), landmarkLocation.latitude, landmarkLocation.longitude, results);
                float distanceInMeters = results[0];

                if (distanceInMeters < radius) {
                    currentlyInArea = true;
                    break;
                }
            }

            if (currentlyInArea && !isInLandmarkArea) {
                getPieceButton.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "ランドマークエリアに入りました！", Toast.LENGTH_SHORT).show();
                isInLandmarkArea = true;
            } else if (!currentlyInArea && isInLandmarkArea) {
                getPieceButton.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "ランドマークエリアから出ました。", Toast.LENGTH_SHORT).show();
                isInLandmarkArea = false;
            }
        });
    }
}