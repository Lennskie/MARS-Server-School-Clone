package mars.logic.domain;

import com.fasterxml.jackson.annotation.JsonProperty;


public class VitalStatus {
    @JsonProperty
    private String status;
    public VitalStatus(String status){
        this.status = status;
    }
}
