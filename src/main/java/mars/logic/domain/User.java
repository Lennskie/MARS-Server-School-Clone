package mars.logic.domain;

import java.util.Objects;

public class User {
    private final String identifier;
    private final String firstname;
    private final String lastname;

    public User(String identifier, String firstname, String lastname) {
        this.identifier = identifier;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(identifier, user.identifier);
    }

    @Override
    public String toString() {
        return "User{" +
                "identifier='" + identifier + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';
    }
}
