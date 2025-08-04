package ru.syncra.entities.enums;

import lombok.Getter;
import ru.syncra.exception.ParserException;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Getter
public enum BankType {

    SBERBANK(Set.of("900", "Сбербанк", "Сбербанк Онлайн", "Сбер"), "b85b1c98-5307-46c6-9acb-5ca27a21b416"),
    TBANK(Set.of("T-Bank", "Т-Банк"), "2c72d88a-943c-4301-9f14-691ca9136aaf"),
    ;

    private Set<String> names;
    private String bankIdCore;

    BankType(Set<String> names, String bankIdCore) {
        this.names = names;
        this.bankIdCore = bankIdCore;
    }

    public static BankType getBankType(String sender) throws ParserException {
        return Arrays.stream(BankType.values())
                .filter(entry -> entry.getNames().contains(sender))
                .findFirst()
                .orElseThrow(() -> new ParserException("Банк по отправителю: '%s' не найден".formatted(sender)));
    }

}
