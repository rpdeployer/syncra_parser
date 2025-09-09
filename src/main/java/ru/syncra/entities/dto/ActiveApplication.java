package ru.syncra.entities.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActiveApplication {

    // GUID payment
    @JsonProperty("id")
    private String id;

    // timestamp - время создания заявки
    @JsonProperty("createdAt")
    private String from;

    // timestamp - время окончания заявки
    @JsonProperty("validTill")
    private String to;

    // string - реквизит который выставлен мерчанту(телефон, номер краты, номер счета)
    @JsonProperty("account")
    private String account;

    // string - тип реквизита(спб, карта и т.д.)
    @JsonProperty("type")
    private String type;

    // decimal - сумма к оплате AmountFrom
    @JsonProperty("amount")
    private BigDecimal amount;

}
