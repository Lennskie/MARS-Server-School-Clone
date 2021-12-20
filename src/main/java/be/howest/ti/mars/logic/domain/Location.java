package be.howest.ti.mars.logic.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Location {
    @JsonProperty("longitude")
    private final double longitude;
    @JsonProperty("latitude")
    private final double latitude;

    public Location(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    @Override
    public String toString() {
        return "Location{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
