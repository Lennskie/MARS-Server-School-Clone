package mars.logic.domain;

import java.util.Objects;

public class Client extends User implements DispatchDestination, DispatchSource {
    private final String identifier;
    private final String firstname;
    private final String lastname;
    private Subscription subscription;
    private Location location;
    private String vitals;

    public Client(String identifier, String firstname, String lastname, Subscription subscription, Location location, String vitals) {
        super(identifier, firstname, lastname);
        this.identifier = identifier;
        this.firstname = firstname;
        this.lastname = lastname;
        this.subscription = subscription;
        this.location = location;
        this.vitals = vitals;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getFirstname() {
        return firstname;
    }

    @Override
    public String getLastname() {
        return lastname;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getVitals() {
        return vitals;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), identifier);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        if (!super.equals(o)) return false;
        Client client = (Client) o;
        return identifier.equals(client.identifier);
    }

    @Override
    public String toString() {
        return "Client{" +
                "identifier='" + identifier + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                "subscription=" + subscription +
                ", location=" + location +
                ", vitals=" + vitals +
                '}';
    }
}
