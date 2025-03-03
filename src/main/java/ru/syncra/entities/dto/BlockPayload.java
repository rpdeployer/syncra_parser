package ru.syncra.entities.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BlockPayload {

    @JsonProperty("deviceId")
    private String deviceId;

    public BlockPayload(String deviceId) {
        this.deviceId = deviceId;
    }
}

