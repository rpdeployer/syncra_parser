package ru.syncra.entities.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BlockPayload {

    @JsonProperty("deviceId")
    private String deviceId;

    @JsonProperty("bankId")
    private String bankId;

    @JsonProperty("salt")
    private String salt;

}

