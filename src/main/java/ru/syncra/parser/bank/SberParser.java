package ru.syncra.parser.bank;

import org.springframework.stereotype.Component;
import ru.syncra.parser.base.BaseParser;

import java.util.regex.Pattern;

@Component
public class SberParser extends BaseParser {

    @Override
    protected Pattern getBlockPattern() {
        return Pattern.compile("([З|з]аблокирован|115-ФЗ|[О|о]бслуживание|[В|в]ыдача|выдачи наличных|[В|в]ыдача карты|[П|п]одтверждение номера телефона)");
    }

    @Override
    protected Pattern getSmsPattern() {
        return Pattern.compile("([\\d]+(?:[.,]\\d{1,2})?)\\s*(р|руб|rub|₽)");
    }

    @Override
    protected Pattern getNotificationPattern() {
        return Pattern.compile(
                "([\\d]+(?:[.,]\\d{1,2})?)\\s*(р|руб|rub|₽)"
        );
    }

}

