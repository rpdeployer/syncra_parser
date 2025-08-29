package ru.syncra.entities.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MessagePayload {

    @JsonProperty("messageId")
    private String messageId;

    @JsonProperty("from")
    private String from;

    @JsonProperty("to")
    private String to;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("message")
    private String message;

    @JsonProperty("isSms")
    private Boolean isSms;

    @JsonProperty("deviceId")
    private String deviceId;

    @JsonProperty("salt")
    private String salt;

}
