package be.howest.ti.mars.logic.domain;

public class Vehicle {
    private boolean occupied;
    private final String identifier;
    private Location location;

    public Vehicle(String identifier, Location location) {
        this.identifier = identifier;
        this.location = location;
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

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "occupied=" + occupied +
                ", identifier='" + identifier + '\'' +
                ", location=" + location +
                '}';
    }
}
