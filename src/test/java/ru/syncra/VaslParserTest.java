package ru.syncra;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.syncra.entities.dto.ParsedMessage;
import ru.syncra.parser.bank.OzonParser;
import ru.syncra.parser.bank.VaslParser;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.syncra.parser.unit.Units.RUB;
import static ru.syncra.parser.unit.Units.TJS;

class VaslParserTest {

    private VaslParser parser = new VaslParser();

    @DisplayName("Тест парсинга уведомлений")
    @ParameterizedTest(name = "{index} -> вход: {0}, ожидаемый результат: {1}")
    @MethodSource("notificationTestData")
    void testNotificationParsing(String text, ParsedMessage expected) {
        ParsedMessage result = parser.parse(text, false);
        assertEquals(expected, result);
    }

    private static Stream<Arguments> notificationTestData() {
        return Stream.of(
                Arguments.of(
                        "Перевод от Сбербанк на сумму 5.51 смн.",
                        new ParsedMessage(TJS, "5.51")
                ),
                Arguments.of("Некорректный текст", null) // Неподходящий текст
        );
    }

}

