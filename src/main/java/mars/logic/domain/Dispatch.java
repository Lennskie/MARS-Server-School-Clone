package mars.logic.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Dispatch {

    @JsonProperty
    private final String identifier;

    @JsonProperty
    private final DispatchSource source;

    @JsonProperty
    private final DispatchDestination destination;

    public Dispatch(String identifier, DispatchSource source, DispatchDestination destination) {
        this.identifier = identifier;
        this.source = source;
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "Dispatch{" +
                "identifier='" + identifier + '\'' +
                ", source=" + source +
                ", destination=" + destination +
                '}';
    }
}
