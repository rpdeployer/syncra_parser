package ru.syncra.entities.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MessageUpdatePayload {

    @JsonProperty("messageId")
    private String messageId;

    @JsonProperty("amount")
    private String amount;

    @JsonProperty("currency")
    private String currency;

}
