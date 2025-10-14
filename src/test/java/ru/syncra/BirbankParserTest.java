package ru.syncra;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.syncra.entities.dto.ParsedMessage;
import ru.syncra.parser.bank.BirbankParser;
import ru.syncra.parser.bank.IdramParser;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.syncra.parser.unit.Units.AMD;
import static ru.syncra.parser.unit.Units.AZN;

class BirbankParserTest {

    private BirbankParser parser = new BirbankParser();

    @DisplayName("Тест парсинга нотификаций")
    @ParameterizedTest(name = "{index} -> вход: {0}, ожидаемый результат: {1}")
    @MethodSource("smsTestData")
    void testNotificationParsing(String text, ParsedMessage expected) {
        ParsedMessage result = parser.parse(text, true);
        assertEquals(expected, result);
    }

    private static Stream<Arguments> smsTestData() {
        return Stream.of(
                Arguments.of(
                        """
                                18.81 AZN
                                Baku
                                5239**7581
                                15:20 14.09.25
                                BALANCE
                                245.52 AZN
                                (c)KB
                                """,
                        new ParsedMessage(AZN, "18.81")
                ),
                Arguments.of("Некорректный текст", null) // Неподходящий текст
        );
    }

}

