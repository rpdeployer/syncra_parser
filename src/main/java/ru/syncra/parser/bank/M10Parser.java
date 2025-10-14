package ru.syncra.parser.bank;

import org.springframework.stereotype.Component;
import ru.syncra.parser.base.BaseParser;

import java.util.regex.Pattern;

@Component
public class M10Parser extends BaseParser {

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
                ".*поступило(\\d*+\\.\\d{2})(\\w{3})"
        );
    }

    @Override
    protected String prepareMessageForNotification(String message) {
        return message.replaceAll("[\\s\u00A0 ]", "")
                .replaceAll(",", "");
    }

}

