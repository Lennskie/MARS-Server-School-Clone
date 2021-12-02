package be.howest.ti.mars.logic.domain;

public class Vehicle {
    private boolean occupied;
    private final String identifier;
    private float latitude;
    private float longitude;

    public Vehicle(String identifier, float latitude, float longitude) {
        this.identifier = identifier;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean isOccupied) {
        this.occupied = isOccupied;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "occupied=" + occupied +
                ", identifier=" + identifier +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
