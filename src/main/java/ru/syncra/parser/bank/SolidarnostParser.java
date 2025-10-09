package ru.syncra.parser.bank;

import org.springframework.stereotype.Component;
import ru.syncra.parser.base.BaseParser;

import java.util.regex.Pattern;

@Component
public class SolidarnostParser extends BaseParser {

    @Override
    protected Pattern getBlockPattern() {
        return Pattern.compile("Блокировка карты.*");
    }

    @Override
    protected Pattern getSmsPattern() {
        return Pattern.compile("(?!)");
    }

    @Override
    protected Pattern getNotificationPattern() {
        return Pattern.compile(
                "(?:Зачислено|Зачисление)\\s*([0-9]+(?:[\\\\.,][0-9]{1,2})?)\\s*([рRUB]{1,3})"
        );
    }

}

