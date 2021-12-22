package mars.logic.domain;

public class Dangerzone {

    private final Location location;
    private final float radius;
    private final String identifier;

    public Dangerzone(String identifier, Location location, float radius){
        this.identifier = identifier;
        this.radius = radius;
        this.location = location;
    }

    @SuppressWarnings("unused")
    public Location getLocation() {
        return location;
    }

    @SuppressWarnings("unused")
    public float getRadius() {
        return radius;
    }

    @SuppressWarnings("unused")
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String toString() {
        return "Dangerzone{" +
                "location=" + location +
                ", radius=" + radius +
                '}';
    }
}
