package ru.syncra.entities.enums;

import lombok.Getter;
import ru.syncra.exception.ParserException;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Getter
public enum BankType {

    SBERBANK(Set.of("900", "Сбербанк")),
    TBANK(Set.of("T-Bank", "Т-Банк")),
    ;

    private Set<String> names;

    BankType(Set<String> names) {
        this.names = names;
    }

    public static BankType getBankType(String sender) throws ParserException {
        return Arrays.stream(BankType.values())
                .filter(entry -> entry.getNames().contains(sender))
                .findFirst()
                .orElseThrow(() -> new ParserException("Банк по отправителю: '%s' не найден".formatted(sender)));
    }

}
