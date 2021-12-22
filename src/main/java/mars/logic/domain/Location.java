package mars.logic.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Location {
    @JsonProperty("latitude")
    private final double latitude;
    @JsonProperty("longitude")
    private final double longitude;


    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;

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
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
