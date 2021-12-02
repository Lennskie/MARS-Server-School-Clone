package be.howest.ti.mars.logic.domain;

public class Client {
    private String firstname;
    private String lastname;

    private Location location;

    private VitalStatus vitals;
    private Subscription subscription;

    private String identifier;

    public Client(String firstname, String lastname, Location location, VitalStatus vitals, Subscription subscription, String identifier) {
        this.firstname = firstname;
        this.lastname = lastname;

        this.location = location;

        this.vitals = vitals;
        this.subscription = subscription;
        this.identifier = identifier;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return "Client{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", location=" + location +
                ", vitals=" + vitals +
                ", subscription=" + subscription +
                ", identifier='" + identifier + '\'' +
                '}';
    }
}
