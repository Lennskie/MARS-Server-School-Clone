package be.howest.ti.mars.logic.domain;

public class Dangerzone {

    private Location location;
    private int radius;

    public Dangerzone(Location location, Integer radius){
        this.radius = radius;
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public int getRadius() {
        return radius;
    }

    @Override
    public String toString() {
        return "Dangerzone{" +
                "location=" + location +
                ", radius=" + radius +
                '}';
    }
}
