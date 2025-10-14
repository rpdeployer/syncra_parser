package ru.syncra;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.syncra.entities.dto.ParsedMessage;
import ru.syncra.exception.BlockingException;
import ru.syncra.parser.bank.IdramParser;
import ru.syncra.parser.bank.SolidarnostParser;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.syncra.parser.unit.Units.AMD;
import static ru.syncra.parser.unit.Units.RUB;

class IdramParserTest {

    private IdramParser parser = new IdramParser();

    @DisplayName("Тест парсинга нотификаций")
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
                                IDBank: MUTQ HASHVIN
                                680.50 AMD
                                MASTER **8185 - MNACORD: 908.30 AMD
                                SBQ, AM 04.10.2025 19:51
                                """,
                        new ParsedMessage(AMD, "680.50")
                ),
                Arguments.of(
                        """
                                IDBank: MUTQ HASHVIN
                                680.50 AMD
                                MASTER **8185 - MNACORD: 908.30 AMD
                                MNACORD 04.10.2025 19:51
                                """,
                        null
                ),
                Arguments.of("Некорректный текст", null) // Неподходящий текст
        );
    }

}

