package mars.logic.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class Vehicle {
    @JsonProperty
    private boolean occupied;
    @JsonProperty
    private final String identifier;
    @JsonProperty
    private Location location;

    public Vehicle(String identifier, boolean occupied, Location location) {
        this.identifier = identifier;
        this.occupied = occupied;
        this.location = location;
    }

    public Vehicle(String identifier) {
        this(identifier, false, null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle)) return false;
        Vehicle vehicle = (Vehicle) o;
        return identifier.equals(vehicle.identifier);
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

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
