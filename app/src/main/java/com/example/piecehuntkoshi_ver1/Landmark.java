package com.example.piecehuntkoshi_ver1;

import com.google.android.gms.maps.model.LatLng;
import java.io.Serializable;

// Serializableをimplementsして、Fragmentに渡せるようにする
public class Landmark implements Serializable {
    private String name;
    private LatLng location;
    private float radius;
    // 将来のための距離フィールド
    private float distance = -1; // -1は未計算を示す

    public Landmark(String name, LatLng location, float radius) {
        this.name = name;
        this.location = location;
        this.radius = radius;
    }

    // GetterとSetter
    public String getName() { return name; }
    public LatLng getLocation() { return location; }
    public float getRadius() { return radius; }
    public float getDistance() { return distance; }
    public void setDistance(float distance) { this.distance = distance; }
}