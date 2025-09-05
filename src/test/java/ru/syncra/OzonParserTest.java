package ru.syncra;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.syncra.entities.dto.ParsedMessage;
import ru.syncra.exception.BlockingException;
import ru.syncra.parser.bank.OzonParser;
import ru.syncra.parser.bank.SberParser;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static ru.syncra.parser.unit.Units.RUB;

class OzonParserTest {

    private OzonParser parser = new OzonParser();

    @DisplayName("Тест парсинга уведомлений")
    @ParameterizedTest(name = "{index} -> вход: {0}, ожидаемый результат: {1}")
    @MethodSource("notificationTestData")
    void testNotificationParsing(String text, ParsedMessage expected) {
        ParsedMessage result = parser.parse(text, false);
        assertEquals(expected, result);
    }

    @DisplayName("Тест блокирующих сообщений")
    @ParameterizedTest(name = "{index} -> вход: {0} (должно выбросить BlockingException)")
    @MethodSource("blockingMessages")
    void testBlockingMessages(String text) {
        assertDoesNotThrow(() -> parser.checkBlockingMessage(text));
    }

    private static Stream<Arguments> smsTestData() {
        return Stream.of(
                Arguments.of(
                        "Пополнение через СБП на 100 ₽. Антон Эдуардович Р. Альфа-Банк. Баланс 100 ₽",
                        new ParsedMessage(RUB, "100")
                ),
                Arguments.of(
                        "Пополнение через СБП на 200 ₽. Антон Эдуардович Р. ВТБ. Баланс 300 ₽",
                        new ParsedMessage(RUB, "200")
                ),
                Arguments.of(
                        "Пополнение через СБП на 1 000 ₽. Варвара Николаевна С. Райффайзен Банк. Баланс 2 012 ₽",
                        new ParsedMessage(RUB, "1000")
                ),
                Arguments.of(
                        "Пополнение через СБП на 1 010 ₽. Владислав Юрьевич М. Т-Банк. Баланс 2 507 ₽",
                        new ParsedMessage(RUB, "1010")
                ),
                Arguments.of(
                        "Пополнение через СБП на 1 005 ₽. Владислав Денисович Л. Т-Банк. Баланс 1 012 ₽",
                        new ParsedMessage(RUB, "1005")
                ),
                Arguments.of(
                        "Пополнение через СБП на 30 ₽. Марк Андреевич Ц. Сбербанк. Баланс 3 092 ₽",
                        new ParsedMessage(RUB, "30")
                ),
                Arguments.of(
                        "Пополнение на 1 000 ₽. Екатерина Леонидовна П.. Доступно 1 497 ₽",
                        new ParsedMessage(RUB, "1000")
                ),
                Arguments.of("Некорректный текст", null) // Неподходящий текст
        );
    }

    private static Stream<Arguments> notificationTestData() {
        return Stream.of(
                Arguments.of(
                        "Пополнение через СБП на 100 ₽. Антон Эдуардович Р. Альфа-Банк. Баланс 100 ₽",
                        new ParsedMessage(RUB, "100")
                ),
                Arguments.of(
                        "Пополнение через СБП на 200 ₽. Антон Эдуардович Р. ВТБ. Баланс 300 ₽",
                        new ParsedMessage(RUB, "200")
                ),
                Arguments.of(
                        "Пополнение через СБП на 1 000 ₽. Варвара Николаевна С. Райффайзен Банк. Баланс 2 012 ₽",
                        new ParsedMessage(RUB, "1000")
                ),
                Arguments.of(
                        "Пополнение через СБП на 1 010 ₽. Владислав Юрьевич М. Т-Банк. Баланс 2 507 ₽",
                        new ParsedMessage(RUB, "1010")
                ),
                Arguments.of(
                        "Пополнение через СБП на 1 005 ₽. Владислав Денисович Л. Т-Банк. Баланс 1 012 ₽",
                        new ParsedMessage(RUB, "1005")
                ),
                Arguments.of(
                        "Пополнение через СБП на 30 ₽. Марк Андреевич Ц. Сбербанк. Баланс 3 092 ₽",
                        new ParsedMessage(RUB, "30")
                ),
                Arguments.of(
                        "Пополнение на 1 000 ₽. Екатерина Леонидовна П.. Доступно 1 497 ₽",
                        new ParsedMessage(RUB, "1000")
                ),
                Arguments.of("Некорректный текст", null) // Неподходящий текст
        );
    }

    private static Stream<Arguments> blockingMessages() {
        return Stream.of(
                Arguments.of("Подтверждение номера телефона в СберБанк Онлайн. Код: 51570. Никому его не сообщайте. Если подтверждаете не вы, позвоните на 900.")
        );
    }

}

