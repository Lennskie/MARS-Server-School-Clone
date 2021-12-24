package mars.logic.domain;

import com.fasterxml.jackson.annotation.JsonProperty;


public class VitalStatus {
    @JsonProperty
    private String status; // @TODO IMPLEMENT VITAL STATUSES

    public VitalStatus(String status){
        this.status = status;
    }
}
