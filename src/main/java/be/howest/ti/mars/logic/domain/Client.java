package be.howest.ti.mars.logic.domain;

public class Client {
    private String firstname;
    private String lastname;

    private float latitude;
    private float longitude;

    private VitalStatus vitals;
    private Subscription subscription;

    private String identifier;

    public Client(String firstname, String lastname, float longitude, float latitude, VitalStatus vitals, Subscription subscription, String identifier) {
        this.firstname = firstname;
        this.lastname = lastname;

        this.latitude = latitude;
        this.longitude = longitude;

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

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
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
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", vitals=" + vitals +
                ", subscription=" + subscription +
                ", identifier='" + identifier + '\'' +
                '}';
    }
}
