package ru.syncra.entities.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ConfirmPayload {

    @JsonProperty("applicationId")
    private String applicationId;

    public ConfirmPayload(String applicationId) {
        this.applicationId = applicationId;
    }
}

