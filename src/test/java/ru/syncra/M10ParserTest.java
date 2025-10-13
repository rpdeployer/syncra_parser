package ru.syncra;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.syncra.entities.dto.ParsedMessage;
import ru.syncra.parser.bank.AmeriaParser;
import ru.syncra.parser.bank.M10Parser;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.syncra.parser.unit.Units.AMD;
import static ru.syncra.parser.unit.Units.AZN;

class M10ParserTest {

    private M10Parser parser = new M10Parser();

    @DisplayName("Тест парсинга нотификации")
    @ParameterizedTest(name = "{index} -> вход: {0}, ожидаемый результат: {1}")
    @MethodSource("notificationTestData")
    void testNotificationParsing(String text, ParsedMessage expected) {
        ParsedMessage result = parser.parse(text, false);
        assertEquals(expected, result);
    }

    private static Stream<Arguments> notificationTestData() {
        return Stream.of(
                Arguments.of(
                        """
                                Пополнение баланса m10
                                На ваш m10 поступило 0.20 AZN
                                """,
                        new ParsedMessage(AZN, "0.20")
                ),
                Arguments.of("Некорректный текст", null) // Неподходящий текст
        );
    }

}

