package be.howest.ti.mars.logic.domain;

public class Location {
    private final float longitude;
    private final float latitude;

    public Location(float longitude, float latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }
}
