package ru.syncra.parser.bank;

import org.springframework.stereotype.Component;
import ru.syncra.parser.base.BaseParser;

import java.util.regex.Pattern;

@Component
public class AkbarsParser extends BaseParser {

    @Override
    protected Pattern getBlockPattern() {
        return Pattern.compile("(?!)");
    }

    @Override
    protected Pattern getSmsPattern() {
        return Pattern.compile(".*пополнениена([0-9]+(?:\\.[0-9]{2})?)([A-Z]{3})");
    }

    @Override
    protected Pattern getNotificationPattern() {
        return Pattern.compile(
                "(?!)"
        );
    }

}

