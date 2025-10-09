package ru.syncra;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.syncra.entities.dto.ParsedMessage;
import ru.syncra.parser.bank.AkbarsParser;
import ru.syncra.parser.bank.SpitamenParser;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.syncra.parser.unit.Units.RUB;
import static ru.syncra.parser.unit.Units.TJS;

class SpitamenParserTest {

    private SpitamenParser parser = new SpitamenParser();

    @DisplayName("Тест парсинга нотификации")
    @ParameterizedTest(name = "{index} -> вход: {0}, ожидаемый результат: {1}")
    @MethodSource("notificationTestData")
    void testNotificationParsing(String text, ParsedMessage expected) {
        ParsedMessage result = parser.parse(text, false);
        assertEquals(expected, result);
    }

    @DisplayName("Тест парсинга SMS сообщений")
    @ParameterizedTest(name = "{index} -> вход: {0}, ожидаемый результат: {1}")
    @MethodSource("smsTestData")
    void testSmsParsing(String text, ParsedMessage expected) {
        ParsedMessage result = parser.parse(text, true);
        assertEquals(expected, result);
    }

    private static Stream<Arguments> notificationTestData() {
        return Stream.of(
                Arguments.of(
                        """
                                Пополнение карты ****2462
                                Сумма: 112.00 TJS - УСПЕШНО
                                Отправитель: КОДИРОВ ГУЛОМЖОН ГОЛИБЖОНОВИЧ
                                Дата: 27.09.2025 17:19:10
                                Код операции: 105016158
                                """,
                        new ParsedMessage(TJS, "112.00")
                ),
                Arguments.of("Некорректный текст", null) // Неподходящий текст
        );
    }

    private static Stream<Arguments> smsTestData() {
        return Stream.of(
                Arguments.of(
                        """
                                UPI/KM2061 08/10/2025 21:22
                                Приход: 61.61 TJS
                                CJSC "Spitamen Bank"; Dushanbe, SpitamenPay 3.0
                                Остаток: 0,18 TJS
                                """,
                        new ParsedMessage(TJS, "61.61")
                ),
                Arguments.of("Некорректный текст", null) // Неподходящий текст
        );
    }

}

