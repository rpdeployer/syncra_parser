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
    OZON(Set.of("Ozon Банк"), "024c9d33-9a5c-42e1-8104-e34a2ca46391"),
    VASL(Set.of("Vasl Pay", "Перевод"), "01961ff4-6d64-7162-ad88-8ca988f1702f"),
    AKBARS(Set.of("AKBARS"), "2526b044-957f-4604-b337-a41fa62683a5"),
    SPITAMEN(Set.of("Spitamen Pay", "BANK"), "01961ff4-6d64-7162-ad99-8ca988f1702f"),
    AMERIA(Set.of("ԱՐՔԱ գործարքներ", "My Ameria"), "0e80dbaa-b2d7-4202-9b60-faa777a22f06"),
    SOLIDARNOST(Set.of("Солидарность"), "953feac6-392c-4692-c2c6-c2ed9ffd9c5b"),
    IDRAM(Set.of("Idram"), "953feac6-392c-4692-c3c7-c2ed9ffd9c5b"),
    M10(Set.of("Birbank"), "953feac6-392c-4692-c3c9-c2ed9ffd9c5b"),
    BIRBANK(Set.of("m10"), "953feac6-392c-4692-c3d1-c2ed9ffd9c5b"),
    MATIN(Set.of("MDO MATIN", "МАТИН"), "953feac6-392c-4692-c3c8-c2ed9ffd9c5b"),
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
