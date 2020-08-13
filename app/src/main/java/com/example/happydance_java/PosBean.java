package com.example.happydance_java;

public class PosBean {
    private double latitude;
    private double longitude;

    public PosBean(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f)",latitude,longitude);
    }
}
