package com.example.piecehuntkoshi_ver1;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import java.io.Serializable;

public class Landmark implements Serializable {
    private String landmarkId;
    private String name;
    private LatLng location;
    private float radius;
    private String description;
    private int imageResourceId;

    private float distance = -1;

    private long lastAcquiredTimestamp = 0;

    private transient Circle googleMapCircle;

    public Landmark(String landmarkId, String name, LatLng location, float radius, String description, int imageResourceId) {
        this.landmarkId = landmarkId;
        this.name = name;
        this.location = location;
        this.radius = radius;
        this.description = description;
        this.imageResourceId = imageResourceId;
    }

    public String getLandmarkId() { return landmarkId; }
    public String getName() { return name; }
    public LatLng getLocation() { return location; }
    public float getRadius() { return radius; }
    public String getDescription() { return description; }
    public int getImageResourceId() { return imageResourceId; }

    public float getDistance() { return distance; }
    public void setDistance(float distance) { this.distance = distance; }

    public long getLastAcquiredTimestamp() { return lastAcquiredTimestamp; }
    public void setLastAcquiredTimestamp(long timestamp) { this.lastAcquiredTimestamp = timestamp; }

    public Circle getGoogleMapCircle() { return googleMapCircle; }
    public void setGoogleMapCircle(Circle circle) { this.googleMapCircle = circle; }

    public boolean isOnCooldown() {
        if (lastAcquiredTimestamp == 0) {
            return false;
        }
        long elapsed = System.currentTimeMillis() - lastAcquiredTimestamp;
        long twentyFourHoursInMillis = 24 * 60 * 60 * 1000;
        return elapsed < twentyFourHoursInMillis;
    }
}