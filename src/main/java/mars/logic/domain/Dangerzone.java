package mars.logic.domain;

public class Dangerzone {

    private final Location location;
    private final int radius;

    public Dangerzone(Location location, Integer radius){
        this.radius = radius;
        this.location = location;
    }

    @SuppressWarnings("unused")
    public Location getLocation() {
        return location;
    }

    @SuppressWarnings("unused")
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
