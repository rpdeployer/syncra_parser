package ru.syncra;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.syncra.entities.dto.ParsedMessage;
import ru.syncra.exception.BlockingException;
import ru.syncra.parser.bank.AkbarsParser;
import ru.syncra.parser.bank.SolidarnostParser;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.syncra.parser.unit.Units.RUB;

class SolidarnostParserTest {

    private SolidarnostParser parser = new SolidarnostParser();

    @DisplayName("Тест парсинга нотификаций")
    @ParameterizedTest(name = "{index} -> вход: {0}, ожидаемый результат: {1}")
    @MethodSource("smsTestData")
    void testNotificationParsing(String text, ParsedMessage expected) {
        ParsedMessage result = parser.parse(text, false);
        assertEquals(expected, result);
    }

    @DisplayName("Тест блокирующих сообщений")
    @ParameterizedTest(name = "{index} -> вход: {0} (должно выбросить BlockingException)")
    @MethodSource("blockingMessages")
    void testBlockingMessages(String text) {
        assertThrows(BlockingException.class, () -> parser.checkBlockingMessage(text));
    }

    private static Stream<Arguments> smsTestData() {
        return Stream.of(
                Arguments.of(
                        "Зачислено 10.0 р из Альфа_Банка",
                        new ParsedMessage(RUB, "10.0")
                ),
                Arguments.of(
                        "*6644 Зачисление 8734.00 RUB.",
                        new ParsedMessage(RUB, "8734.00")
                ),
                Arguments.of("Некорректный текст", null) // Неподходящий текст
        );
    }

    private static Stream<Arguments> blockingMessages() {
        return Stream.of(
                Arguments.of("Блокировка карты *6644: по заявлению клиента")
        );
    }

}

