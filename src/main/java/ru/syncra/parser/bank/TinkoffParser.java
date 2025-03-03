package ru.syncra.parser.bank;

import org.springframework.stereotype.Component;
import ru.syncra.parser.base.BaseParser;

import java.util.regex.Pattern;

@Component
public class TinkoffParser extends BaseParser {

    @Override
    protected Pattern getBlockPattern() {
        return null;
    }

    @Override
    protected Pattern getSmsPattern() {
        return Pattern.compile("");
    }

    @Override
    protected Pattern getNotificationPattern() {
        return Pattern.compile(
                "(?i)(?:summa)\\s*([\\d]+(?:[.,]\\d{1,2})?)\\s*(р|руб|rub|₽)?"
        );
    }

}

