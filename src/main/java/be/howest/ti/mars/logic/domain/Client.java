package be.howest.ti.mars.logic.domain;

import java.util.Objects;

public class Client extends User {

    private Location location;

    private VitalStatus vitals;
    private Subscription subscription;

    public Client(String firstname, String lastname, Location location, VitalStatus vitals, Subscription subscription, String identifier) {
        super(firstname, lastname, identifier);

        this.location = location;

        this.vitals = vitals;
        this.subscription = subscription;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public VitalStatus getVitals() {
        return vitals;
    }

    public void setVitals(VitalStatus vitals) {
        this.vitals = vitals;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    @Override
    public String toString() {
        return "Client{" +
                "firstname='" + super.getFirstname() + '\'' +
                ", lastname='" + super.getLastname() + '\'' +
                ", location=" + location +
                ", vitals=" + vitals +
                ", subscription=" + subscription +
                ", identifier='" + super.getIdentifier() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(location, client.location) && Objects.equals(vitals, client.vitals) && Objects.equals(subscription, client.subscription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, vitals, subscription);
    }
}
