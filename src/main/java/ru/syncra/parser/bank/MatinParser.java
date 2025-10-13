package ru.syncra.parser.bank;

import org.springframework.stereotype.Component;
import ru.syncra.entities.dto.ParsedMessage;
import ru.syncra.parser.base.BaseParser;
import ru.syncra.parser.unit.Units;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MatinParser extends BaseParser {

    @Override
    protected Pattern getBlockPattern() {
        return Pattern.compile("(?!)");
    }

    @Override
    protected Pattern getSmsPattern() {
        return Pattern.compile(
                ".*Пополнениякошелька.*?Сумма(TJS)[:\\s]*([0-9]+(?:[.,][0-9]{1,2})?)"
        );
    }

    @Override
    protected Pattern getNotificationPattern() {
        return Pattern.compile(
                ".*Поступлениесредств(\\d*+\\.\\d{2})(\\w{3})"
        );
    }

    @Override
    protected String prepareMessageForNotification(String message) {
        return message.replaceAll("[\\s\u00A0 ]", "")
                .replaceAll(",", "")
                .replaceAll("Balance.*", "");
    }

    @Override
    protected ParsedMessage checkBankMessage(String text, boolean isSms) {
        Matcher matcher;
        if (isSms) {
            String input = prepareMessageForSMS(text);
            matcher = getSmsPattern().matcher(input);
        } else {
            String input = prepareMessageForNotification(text);
            matcher = getNotificationPattern().matcher(input);
        }
        if (matcher.find()) {
            if (isSms) {
                return new ParsedMessage(Units.getUnit(matcher.group(1)), matcher.group(2));
            } else {
                return new ParsedMessage(Units.getUnit(matcher.group(2)), matcher.group(1));
            }
        }
        return null;
    }

}

