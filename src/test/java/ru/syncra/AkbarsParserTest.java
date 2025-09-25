package ru.syncra;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.syncra.entities.dto.ParsedMessage;
import ru.syncra.parser.bank.AkbarsParser;
import ru.syncra.parser.bank.VaslParser;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.syncra.parser.unit.Units.RUB;
import static ru.syncra.parser.unit.Units.TJS;

class AkbarsParserTest {

    private AkbarsParser parser = new AkbarsParser();

    @DisplayName("Тест парсинга sms")
    @ParameterizedTest(name = "{index} -> вход: {0}, ожидаемый результат: {1}")
    @MethodSource("smsTestData")
    void testNotificationParsing(String text, ParsedMessage expected) {
        ParsedMessage result = parser.parse(text, true);
        assertEquals(expected, result);
    }

    private static Stream<Arguments> smsTestData() {
        return Stream.of(
                Arguments.of(
                        "Карта*9905 пополнение на 6027.00RUR.T-Bank Card2Card. 27.07.25 21:16. Остаток 11409.72RUR",
                        new ParsedMessage(RUB, "6027.00")
                ),Arguments.of(
                        "Карта*9905 пополнение на 5001.00RUR.SBOL. 27.07.25 12:56. Остаток 5392.72RUR",
                        new ParsedMessage(RUB, "5001.00")
                ),Arguments.of(
                        "Карта*9905 пополнение на 2015.00RUR.VTB. 09.07.25 19:07. Остаток 11931.53RUR",
                        new ParsedMessage(RUB, "2015.00")
                ),Arguments.of(
                        "Карта*5582 пополнение на 5006.00RUR. Перевод от 220053**2579.AK BARS online. 18.08.25 21:53. Остаток 5214.29RUR",
                        new ParsedMessage(RUB, "5006.00")
                ),Arguments.of(
                        "Карта*5582 Списание с карты 10100.00RUR. KAZAN, AK BARS online. 21.06.25 00:01. Остаток 2222.52RUR",
                        null
                ),
                Arguments.of("Некорректный текст", null) // Неподходящий текст
        );
    }

}

