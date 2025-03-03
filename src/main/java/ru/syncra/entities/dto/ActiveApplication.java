package ru.syncra.entities.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActiveApplication {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("from")
    private String from;

    @JsonProperty("to")
    private String to;

    @JsonProperty("account")
    private String account;

    @JsonProperty("type")
    private String type;

    @JsonProperty("amount")
    private BigDecimal amount;

}
