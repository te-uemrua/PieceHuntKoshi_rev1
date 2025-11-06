package com.example.piecehuntkoshi_ver1;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import java.io.Serializable;

// Serializableをimplementsして、Fragmentに渡せるようにする
public class Landmark implements Serializable {
    private String landmarkId;
    private String name;
    private LatLng location;
    private float radius;
    private String description; // ★★★ 説明文を追加 ★★★
    private int imageResourceId; // ★★★ 画像のリソースIDを追加 ★★★

    // 将来のための距離フィールド
    private float distance = -1; // -1は未計算を示す

    // 最後に取得した時刻（ミリ秒）
    private long lastAcquiredTimestamp = 0;

    // マップ上の円を保持（色を変えるため）
    // transient = Serializable (シリアライズ) の対象外にする
    private transient Circle googleMapCircle;

    // ★★★ コンストラクタを修正 ★★★
    public Landmark(String landmarkId, String name, LatLng location, float radius, String description, int imageResourceId) {
        this.landmarkId = landmarkId;
        this.name = name;
        this.location = location;
        this.radius = radius;
        this.description = description; // ★★★ 追加 ★★★
        this.imageResourceId = imageResourceId; // ★★★ 追加 ★★★
    }

    // --- Getter ---
    public String getLandmarkId() { return landmarkId; }
    public String getName() { return name; }
    public LatLng getLocation() { return location; }
    public float getRadius() { return radius; }
    public String getDescription() { return description; } // ★★★ 追加 ★★★
    public int getImageResourceId() { return imageResourceId; } // ★★★ 追加 ★★★

    public float getDistance() { return distance; }
    public void setDistance(float distance) { this.distance = distance; }

    public long getLastAcquiredTimestamp() { return lastAcquiredTimestamp; }
    public void setLastAcquiredTimestamp(long timestamp) { this.lastAcquiredTimestamp = timestamp; }

    public Circle getGoogleMapCircle() { return googleMapCircle; }
    public void setGoogleMapCircle(Circle circle) { this.googleMapCircle = circle; }

    // ★★★ 24時間クールタイム中か判定するヘルパーメソッド ★★★
    public boolean isOnCooldown() {
        if (lastAcquiredTimestamp == 0) {
            return false; // まだ取得されていない
        }
        long elapsed = System.currentTimeMillis() - lastAcquiredTimestamp;
        // 24時間（ミリ秒）
        long twentyFourHoursInMillis = 24 * 60 * 60 * 1000;
        return elapsed < twentyFourHoursInMillis;
    }
}