package mars.logic.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Dispatch {

    @JsonProperty
    private final DispatchSource source;

    @JsonProperty
    private final DispatchTarget destination;

    public Dispatch(DispatchSource source, DispatchTarget destination) {
        this.source = source;
        this.destination = destination;
    }
}
