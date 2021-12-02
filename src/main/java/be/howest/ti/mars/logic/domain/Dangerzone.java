package be.howest.ti.mars.logic.domain;

public class Dangerzone {

    private int latitude;
    private int longitude;
    private int radius;

    public Dangerzone(Integer latitude, Integer longitude, Integer radius){
        this.radius = radius;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int getLatitude() {
        return latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public int getRadius() {
        return radius;
    }

    @Override
    public String toString() {
        return "Dangerzone{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", radius=" + radius +
                '}';
    }
}
