package ru.syncra.parser.bank;

import org.springframework.stereotype.Component;
import ru.syncra.parser.base.BaseParser;

import java.util.regex.Pattern;

@Component
public class OzonParser extends BaseParser {

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
                "Пополнение(?:черезСБП)?на([0-9]+)\\s*(₽)"
        );
    }

}

