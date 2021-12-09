package be.howest.ti.mars.logic.domain;

public class User {
    private String firstname;
    private String lastname;

    private String identifier;

    public User(String firstname, String lastname, String identifier) {
        this.firstname = firstname;
        this.lastname = lastname;

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
                ", identifier='" + identifier + '\'' +
                '}';
    }
}
