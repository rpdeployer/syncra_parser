package ru.syncra;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.syncra.entities.dto.ParsedMessage;
import ru.syncra.parser.bank.AmeriaParser;
import ru.syncra.parser.bank.SpitamenParser;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.syncra.parser.unit.Units.AMD;
import static ru.syncra.parser.unit.Units.TJS;

class AmeriaParserTest {

    private AmeriaParser parser = new AmeriaParser();

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
                                CREDIT ACCOUNT | 20,500.00 AMD |
                                4083****2324, | AMERIABANK API GATE,
                                AM | 25.09.2025 12:42 | BALANCE:
                                23,309.26 AMD
                                """,
                        new ParsedMessage(AMD, "20500.00")
                ),
                Arguments.of(
                        """
                                CREDIT ACCOUNT | 9,500.30 AMD |
                                4083****2324, | AMERIABANK API GATE,
                                AM | 25.09.2025 12:42 | BALANCE:
                                23,309.26 AMD
                                """,
                        new ParsedMessage(AMD, "9500.30")
                ),
                Arguments.of(
                        """
                                CREDIT ACCOUNT | 500.30 AMD |
                                4083****2324, | AMERIABANK API GATE,
                                AM | 25.09.2025 12:42 | BALANCE:
                                23,309.26 AMD
                                """,
                        new ParsedMessage(AMD, "500.30")
                ),
                Arguments.of("Некорректный текст", null) // Неподходящий текст
        );
    }

}

