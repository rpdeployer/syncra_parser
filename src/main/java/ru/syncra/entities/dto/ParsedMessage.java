package ru.syncra.entities.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ParsedMessage {

    @JsonProperty("amount")
    private String amount;

    @JsonProperty("currency")
    private String currency;

    public ParsedMessage(String currency, String amount) {
        this.currency = currency;
        this.amount = amount;
    }

}

