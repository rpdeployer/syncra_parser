package ru.syncra.parser.base;

import ru.syncra.entities.dto.ParsedMessage;
import ru.syncra.exception.BlockingException;
import ru.syncra.parser.unit.Units;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class BaseParser implements BankParser {

    protected abstract Pattern getBlockPattern();

    protected abstract Pattern getSmsPattern();

    protected abstract Pattern getNotificationPattern();

    @Override
    public ParsedMessage parse(String text, boolean isSms) {
        return checkBankMessage(text, isSms);
    }

    @Override
    public void checkBlockingMessage(String text) throws BlockingException {
        Matcher matcher = getBlockPattern().matcher(text);
        if (matcher.find()) {
            throw new BlockingException();
        }
    }

    private ParsedMessage checkBankMessage(String text, boolean isSms) {
        Matcher matcher;
        if (isSms) {
            String input = prepareMessageForSMS(text);
            matcher = getSmsPattern().matcher(input);
        } else {
            String input = prepareMessageForNotification(text);
            matcher = getNotificationPattern().matcher(input);
        }
        if (matcher.find()) {
            return new ParsedMessage(Units.getUnit(matcher.group(2)), matcher.group(1));
        }
        return null;
    }

    private String prepareMessageForNotification(String message) {
        return message.replaceAll("[\\s\u00A0 ]", "")
                .replaceAll("[Б|б]аланс.*", "")
                .replaceAll(",", ".")
                .replaceAll("[Д|д]оступно.*", ".");
    }

    private String prepareMessageForSMS(String message) {
        return message.replaceAll("[\\s\u00A0]", "")
                .replaceAll("[Б|б]аланс.*", "")
                .replaceAll("[О|о]статок.*", "");
    }

}
