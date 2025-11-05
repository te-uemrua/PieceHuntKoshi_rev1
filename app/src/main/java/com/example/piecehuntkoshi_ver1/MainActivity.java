package com.example.piecehuntkoshi_ver1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    public static final String PREFS_NAME = "PieceHuntPrefs";
    public static final String LAST_ACQUIRED_PREFIX = "_last_acquired";

    private static final int COLOR_RED_TRANSLUCENT = 0x55ff0000;
    private static final int COLOR_BLUE_TRANSLUCENT = 0x550000FF;

    private GoogleMap mMap;
    private Button getPieceButton;
    private Button collectionButton;
    private Button viewPuzzleButton;
    private AnimationDrawable rainbowAnimation;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    private ArrayList<Landmark> landmarkList = new ArrayList<>();
    private Landmark currentActiveLandmark = null;
    private String currentAreaState = "NONE";
    private LandmarkListBottomSheet bottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPieceButton = findViewById(R.id.get_piece_button);
        getPieceButton.setOnClickListener(v -> {
            if (currentActiveLandmark != null && !currentActiveLandmark.isOnCooldown()) {
                Intent intent = new Intent(MainActivity.this, shake_phone.class);
                intent.putExtra("LANDMARK_ID", currentActiveLandmark.getLandmarkId());
                intent.putExtra("LANDMARK_NAME", currentActiveLandmark.getName());
                startActivity(intent);
            }
        });

        collectionButton = findViewById(R.id.collection_button);
        collectionButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CollectionActivity.class);
            startActivity(intent);
        });

        viewPuzzleButton = findViewById(R.id.view_puzzle_button);
        viewPuzzleButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PuzzleActivity.class);
            intent.putExtra("PUZZLE_ID", 1);
            startActivity(intent);
        });

        FloatingActionButton listButton = findViewById(R.id.list_button);
        listButton.setOnClickListener(v -> {
            if (bottomSheet == null) {
                bottomSheet = LandmarkListBottomSheet.newInstance(landmarkList);
            }
            if (!bottomSheet.isAdded()) {
                bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
            }
        });

        initializeLandmarks();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Location lastLocation = locationResult.getLastLocation();
                if (lastLocation != null) {
                    checkDistanceToLandmarks(lastLocation);
                }
            }
        };

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initializeLandmarks() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        landmarkList.add(createLandmarkWithTimestamp(prefs, "amby_kumamoto", "アンビー熊本", new LatLng(32.880783,130.785207),200f));
        landmarkList.add(createLandmarkWithTimestamp(prefs, "classsino_marche", "クラッシーノマルシェ", new LatLng(32.891637,130.732765),200f));
        landmarkList.add(createLandmarkWithTimestamp(prefs, "country_park", "熊本県農業カントリーパーク", new LatLng(32.8900575, 130.7595619), 500f));
        landmarkList.add(createLandmarkWithTimestamp(prefs, "genki_no_mori", "元気の森公園", new LatLng(32.866013,130.76833), 400f));
        landmarkList.add(createLandmarkWithTimestamp(prefs, "goshijuku_ato", "合志義塾跡", new LatLng(32.9163671, 130.7458907), 200f));
        landmarkList.add(createLandmarkWithTimestamp(prefs, "manga_museum", "合志マンガミュージアム", new LatLng(32.891069,130.745138), 200f));
        landmarkList.add(createLandmarkWithTimestamp(prefs, "spring_garden", "スプリングガーデン御代志", new LatLng(32.880799,130.748208), 500f));
        landmarkList.add(createLandmarkWithTimestamp(prefs, "takaba_jyouato", "竹迫城跡公園", new LatLng(32.89896389, 130.79429999), 200f));
        landmarkList.add(createLandmarkWithTimestamp(prefs, "myosenji_park", "妙泉寺公園", new LatLng(32.858651,130.732413), 100f));
        landmarkList.add(createLandmarkWithTimestamp(prefs, "yumemall_koshi", "ゆめモール合志", new LatLng(32.902321,130.762525), 100f));
    }

    private Landmark createLandmarkWithTimestamp(SharedPreferences prefs, String id, String name, LatLng location, float radius) {
        Landmark landmark = new Landmark(id, name, location, radius);
        long lastAcquired = prefs.getLong(id + LAST_ACQUIRED_PREFIX, 0);
        landmark.setLastAcquiredTimestamp(lastAcquired);
        return landmark;
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadDataAndRefreshMap();
        startLocationChecks();
    }

    private void reloadDataAndRefreshMap() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean needsRefresh = false;

        for (Landmark landmark : landmarkList) {
            long newTimestamp = prefs.getLong(landmark.getLandmarkId() + LAST_ACQUIRED_PREFIX, 0);

            boolean wasOnCooldown = landmark.isOnCooldown();
            landmark.setLastAcquiredTimestamp(newTimestamp);
            boolean isNowOnCooldown = landmark.isOnCooldown();

            if (wasOnCooldown != isNowOnCooldown) {
                updateMapCircleColor(landmark);
                needsRefresh = true;
            } else if (landmark.getLastAcquiredTimestamp() != newTimestamp) {
                needsRefresh = true;
            }
        }

        if (needsRefresh && bottomSheet != null && bottomSheet.getAdapter() != null) {
            bottomSheet.getAdapter().notifyDataSetChanged();
        }
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
            startLocationChecks();
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
                startLocationChecks();
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
        for (Landmark landmark : landmarkList) {
            mMap.addMarker(new MarkerOptions().position(landmark.getLocation()).title(landmark.getName()));

            CircleOptions circleOptions = new CircleOptions()
                    .center(landmark.getLocation())
                    .radius(landmark.getRadius())
                    .strokeColor(Color.RED)
                    .strokeWidth(5f);

            Circle circle = mMap.addCircle(circleOptions);
            landmark.setGoogleMapCircle(circle);

            updateMapCircleColor(landmark);
        }

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

    private void updateMapCircleColor(Landmark landmark) {
        if (landmark.getGoogleMapCircle() == null) {
            return;
        }

        if (landmark.isOnCooldown()) {
            landmark.getGoogleMapCircle().setFillColor(COLOR_BLUE_TRANSLUCENT);
        } else {
            landmark.getGoogleMapCircle().setFillColor(COLOR_RED_TRANSLUCENT);
        }
    }

    private void startLocationChecks() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    private void stopLocationChecks() {
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    private void checkDistanceToLandmarks(Location location) {

        Landmark closestLandmarkInArea = null;
        float minDistance = Float.MAX_VALUE;
        boolean needsListUpdate = false;

        for (Landmark landmark : landmarkList) {
            float[] results = new float[1];
            Location.distanceBetween(
                    location.getLatitude(), location.getLongitude(),
                    landmark.getLocation().latitude, landmark.getLocation().longitude,
                    results);
            float distanceInMeters = results[0];

            if (landmark.getDistance() != distanceInMeters) {
                needsListUpdate = true;
            }
            landmark.setDistance(distanceInMeters);

            if (distanceInMeters < landmark.getRadius()) {
                if (distanceInMeters < minDistance) {
                    minDistance = distanceInMeters;
                    closestLandmarkInArea = landmark;
                }
            }
        }

        if (needsListUpdate && bottomSheet != null && bottomSheet.getAdapter() != null) {
            bottomSheet.getAdapter().notifyDataSetChanged();
        }

        String newState;
        if (closestLandmarkInArea != null) {
            if (closestLandmarkInArea.isOnCooldown()) {
                newState = "COOLDOWN";
                currentActiveLandmark = closestLandmarkInArea;
            } else {
                newState = "AVAILABLE";
                currentActiveLandmark = closestLandmarkInArea;
            }
        } else {
            newState = "NONE";
            currentActiveLandmark = null;
        }

        if (newState.equals(currentAreaState)) {
            return;
        }

        currentAreaState = newState;

        if (getPieceButton == null) return;

        switch (newState) {
            case "AVAILABLE":
                getPieceButton.setBackgroundResource(R.drawable.rainbow_animation);
                rainbowAnimation = (AnimationDrawable) getPieceButton.getBackground();
                if (rainbowAnimation != null) rainbowAnimation.start();

                getPieceButton.setVisibility(View.VISIBLE);
                getPieceButton.setText("ピースをゲット!!");
                getPieceButton.setEnabled(true);

                Toast.makeText(MainActivity.this, currentActiveLandmark.getName() + " のエリアに入りました！", Toast.LENGTH_SHORT).show();
                break;

            case "COOLDOWN":
                if (rainbowAnimation != null) rainbowAnimation.stop();
                rainbowAnimation = null;

                getPieceButton.setBackgroundResource(R.drawable.bordered_button_background);
                getPieceButton.setVisibility(View.VISIBLE);
                getPieceButton.setText("クールタイム中");
                getPieceButton.setEnabled(false);

                Toast.makeText(MainActivity.this, "クールタイム中のエリアです", Toast.LENGTH_SHORT).show();
                break;

            case "NONE":
            default:
                getPieceButton.setVisibility(View.GONE);
                if (rainbowAnimation != null) rainbowAnimation.stop();
                rainbowAnimation = null;

                getPieceButton.setBackgroundResource(R.drawable.bordered_button_background);
                getPieceButton.setEnabled(true);
                break;
        }
    }
}