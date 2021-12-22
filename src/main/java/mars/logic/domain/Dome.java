package mars.logic.domain;

import java.util.Objects;

public class Dome implements DispatchDestination {
    private final String identifier;
    private final int size;
    private Location location;

    public Dome(String identifier, int size, Location location) {
        this.identifier = identifier;
        this.size = size;
        this.location = location;
    }

    public Dome(String identifier) {
        this(identifier,0, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dome)) return false;
        Dome dome = (Dome) o;
        return identifier.equals(dome.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "identifier='" + identifier + '\'' +
                '}';
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getSize() {
        return size;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
