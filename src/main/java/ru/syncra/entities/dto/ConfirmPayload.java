package ru.syncra.entities.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConfirmPayload {

    @JsonProperty("paymentId")
    private String paymentId;

    @JsonProperty("messageId")
    private String messageId;

    @JsonProperty("salt")
    private String salt;

}

