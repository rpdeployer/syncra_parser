package ru.syncra;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.syncra.entities.dto.ParsedMessage;
import ru.syncra.parser.bank.M10Parser;
import ru.syncra.parser.bank.MatinParser;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.syncra.parser.unit.Units.AZN;
import static ru.syncra.parser.unit.Units.TJS;

class MatinParserTest {

    private MatinParser parser = new MatinParser();

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
                                МАТИН
                                Поступление средств 11.66 TJS
                                10.10.2025 14:49
                                """,
                        new ParsedMessage(TJS, "11.66")
                ),
                Arguments.of(
                        """
                                МАТИН
                                Поступление средств 11.56 TJS
                                10.10.2025 14:57
                                """,
                        new ParsedMessage(TJS, "11.56")
                ),
                Arguments.of("Некорректный текст", null) // Неподходящий текст
        );
    }

    private static Stream<Arguments> smsTestData() {
        return Stream.of(
                Arguments.of(
                        """
                                Пополнения кошелька
                                Сервис: ВТБ Банк
                                Сумма RUB: 100.00
                                Сумма TJS: 11.74
                                Дата: 2025.10.10 12:43:12
                                """,
                        new ParsedMessage(TJS, "11.74")
                ),
                Arguments.of(
                        """
                                Пополнения кошелька
                                Сервис: Т-Банк
                                Сумма RUB: 100
                                Сумма TJS: 11.66
                                Дата: 2025.10.10 12:51:20
                                """,
                        new ParsedMessage(TJS, "11.66")
                ),
                Arguments.of("Некорректный текст", null) // Неподходящий текст
        );
    }

}

