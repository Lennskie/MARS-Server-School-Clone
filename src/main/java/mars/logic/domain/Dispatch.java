package mars.logic.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Dispatch {

    @JsonProperty
    private final String identifier;

    @JsonProperty
    private final DispatchSource source;

    @JsonProperty
    private final DispatchTarget destination;

    public Dispatch(String identifier, DispatchSource source, DispatchTarget destination) {
        this.identifier = identifier;
        this.source = source;
        this.destination = destination;
    }
}
