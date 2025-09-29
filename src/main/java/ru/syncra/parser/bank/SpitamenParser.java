package ru.syncra.parser.bank;

import org.springframework.stereotype.Component;
import ru.syncra.parser.base.BaseParser;

import java.util.regex.Pattern;

@Component
public class SpitamenParser extends BaseParser {

    @Override
    protected Pattern getBlockPattern() {
        return Pattern.compile("(?!)");
    }

    @Override
    protected Pattern getSmsPattern() {
        return Pattern.compile("(?!)");
    }

    @Override
    protected Pattern getNotificationPattern() {
        return Pattern.compile(
                "Пополнениекарты\\*{4}\\d+.*?Сумма:\\s*([0-9]+(?:\\.[0-9]{2})?)\\s*([A-Z]{3})"
        );
    }

}

