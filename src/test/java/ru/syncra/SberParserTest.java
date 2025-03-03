package ru.syncra;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.syncra.entities.dto.ParsedMessage;
import ru.syncra.exception.BlockingException;
import ru.syncra.parser.bank.SberParser;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static ru.syncra.parser.unit.Units.RUB;

class SberParserTest {

    private SberParser parser = new SberParser();

    @DisplayName("Тест парсинга SMS сообщений")
    @ParameterizedTest(name = "{index} -> вход: {0}, ожидаемый результат: {1}")
    @MethodSource("smsTestData")
    void testSmsParsing(String text, ParsedMessage expected) {
        ParsedMessage result = parser.parse(text, true);
        assertEquals(expected, result);
    }

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
        assertThrows(BlockingException.class, () -> parser.checkBlockingMessage(text));
    }

    @DisplayName("Тест блокирующих сообщений(негативный)")
    @ParameterizedTest(name = "{index} -> вход: {0} (должно выбросить BlockingException)")
    @MethodSource("negativeBlockingMessages")
    void testNegativeBlockingMessages(String text) {
        assertDoesNotThrow(() -> parser.checkBlockingMessage(text));
    }

    private static Stream<Arguments> smsTestData() {
        return Stream.of(
                Arguments.of(
                        "СЧЁТ4006 21:21 Перевод 7000р от Сардорбек Ж. Баланс: 51 110.2р ",
                        new ParsedMessage(RUB, "7000")
                ),
                Arguments.of(
                        "СЧЁТ4006 21:20 Перевод 5001.01р от Игорь К. Баланс: 44 110.2р",
                        new ParsedMessage(RUB, "5001.01")
                ),
                Arguments.of(
                        "СЧЁТ4006 21:01 Перевод 5100р от Олег К. Баланс: 74 171.2р",
                        new ParsedMessage(RUB, "5100")
                ),
                Arguments.of(
                        "СЧЁТ4006 21:00 Перевод 5004р от Сергей З. Баланс: 69 071.2р",
                        new ParsedMessage(RUB, "5004")
                ),
                Arguments.of(
                        "VISA2059 23:34 Перевод из Альфа-Банк +10р от АНДРЕЙ Ф. Баланс: 102.47р «Перевод денежных средств»",
                        new ParsedMessage(RUB, "10")
                ),
                Arguments.of(
                        "СЧЁТ0129 23:49 Перевод из Альфа-Банк +1000р от АНДРЕЙ Ф. Баланс: 1102.47р «Перевод денежных средств»",
                        new ParsedMessage(RUB, "1000")
                ),
                Arguments.of(
                        "СЧЁТ0129 23:53 Перевод из ВТБ +510р от АНДРЕЙ Ф. Баланс: 1612.47р",
                        new ParsedMessage(RUB, "510")
                ),
                Arguments.of(
                        "VISA2059 13:44 Перевод из Альфа-Банк +3000р от НАРЕК Б. Баланс: 5832.65р «Перевод денежных средств»",
                        new ParsedMessage(RUB, "3000")
                ),
                Arguments.of(
                        "VISA2059 22:43 Перевод из Альфа-Банк +5000р от ЭРИК Б. Баланс: 63769.54р «Перевод денежных средств»",
                        new ParsedMessage(RUB, "5000")
                ),
                Arguments.of(
                        "СЧЁТ0129 20:10 Перевод 632р от Светлана Г. Баланс: 673.42р",
                        new ParsedMessage(RUB, "632")
                ),
                Arguments.of(
                        "VISA2059 18:35 Перевод из Ozon банк +100р от НАРЕК Б. Баланс: 2012.47р",
                        new ParsedMessage(RUB, "100")
                ),
                Arguments.of(
                        "VISA0807 16:07 Перевод 2000р от Елена О. Баланс: 2103.73р «Лёшенька, с Новым годом! »",
                        new ParsedMessage(RUB, "2000")
                ),
                Arguments.of(
                        "VISA2059 18:30 зачисление 500р Т-Bank Баланс: 2412.47р\n" +
                                "\n" +
                                "\n" +
                                "VISA2059 18:30 Андрей Ф. перевел(а) вам 500р.",
                        new ParsedMessage(RUB, "500")
                ),
                Arguments.of(
                        "VISA2059 26.02.25 21:19 зачисление 400р VISA MONEY TRANSFER Баланс: 1912.47р",
                        new ParsedMessage(RUB, "400")
                ),
                Arguments.of("Некорректный текст", null) // Неподходящий текст
        );
    }

    private static Stream<Arguments> notificationTestData() {
        return Stream.of(
                Arguments.of(
                        "СЧЁТ7533 18:00 Перевод из Альфа-Банк +1000р от АНДРЕЙ Ф. Баланс: 1217.04р «Перевод денежных средств»",
                        new ParsedMessage(RUB, "1000")
                ),
                Arguments.of(
                        "СЧЁТ7533 18:01 Перевод из Т-Банк +500.67р от АНДРЕЙ Ф. Баланс: 1717.04р",
                        new ParsedMessage(RUB, "500.67")
                ),
                Arguments.of(
                        "СЧЁТ4006 21:17 Перевод из АБ РОССИЯ +5123,01р от КИРИЛЛ К. Баланс: 39109.20р ",
                        new ParsedMessage(RUB, "5123.01")
                ),
                Arguments.of(
                        "Перевод от МАРИНА АЛЕКСАНДРОВНА Д. Банк Уралсиб + 600 ₽ - Баланс: 6 223,34 ₽ СЧЕТ • 7533",
                        new ParsedMessage(RUB, "600")
                ),
                Arguments.of(
                        "Перевод от МАРИНА АЛЕКСАНДРОВНА Д. Газпромбанк + 1 000 ₽ - Баланс: 7 223,34 ₽ СЧЁТ •• 7533",
                        new ParsedMessage(RUB, "1000")
                ),
                Arguments.of(
                        "Перевод от НАРЕК ДАВИДОВИЧ Б. + 100 ₽ Visa •• 2059 Баланс: 2 012,47 ₽",
                        new ParsedMessage(RUB, "100")
                ),
                Arguments.of(
                        "Зачисление из Т-Bank VISA2059 19:17 Андрей Ф. перевел(а) вам 10000р.",
                        new ParsedMessage(RUB, "10000")
                ),
                Arguments.of(
                        "[27.02.2025 в 19:36] Перевод от Алина Алановна Д. +100р",
                        new ParsedMessage(RUB, "100")
                ),
                Arguments.of(
                        "[27.02.2025 в 19:36] Перевод от Алина Алановна Д. +100.23р",
                        new ParsedMessage(RUB, "100.23")
                ),
                Arguments.of("Некорректный текст", null) // Неподходящий текст
        );
    }

    private static Stream<Arguments> blockingMessages() {
        return Stream.of(
                Arguments.of("СберБанк Онлайн заблокирован по условиям банковского обслуживания из-за подозрений в совершении сомнительных операций. При поступлении дополнительного уведомления от банка следуйте указанным рекомендациям.\n" +
                        "\n" +
                        "\n" +
                        "Андрей Александрович, карта и доступ в СберБанк Онлайн заблокированы. В соответствии с п.14 ст.7 ФЗ №115-ФЗ от 07.08.01 просим в срок до 30.05.2024 предоставить в банк документы и сведения согласно запросу. Получить запрос вы можете в любом офисе банка. Запрос высылается на вашу электронную почту при наличии ее адреса в базах данных банка. Если вам потребуются денежные средства, обратитесь в офис банка. СберБанк"),
                Arguments.of("Вы начали обслуживание в СберБанке в 14:04 мск. Если вы не обращались в банк, позвоните на 900."),
                Arguments.of("Сберегательный счет *7120 выдача 2 079 000р. Введите код 67274 на устройстве сотрудника. Никому его не сообщайте."),
                Arguments.of("Для подписания чека выдачи наличных на 84 578р с Плат. счёта *1046 введите код 85197 на устройстве сотрудника. Никому его не сообщайте."),
                Arguments.of("Выдача карты MIR5742. Введите код 41034 на терминале. Никому его не сообщайте."),
                Arguments.of("Подтверждение номера телефона в СберБанк Онлайн. Код: 51570. Никому его не сообщайте. Если подтверждаете не вы, позвоните на 900.\n")
        );
    }

    private static Stream<Arguments> negativeBlockingMessages() {
        return Stream.of(
                Arguments.of("[27.02.2025 в 19:36] Перевод от Алина Алановна Д. +100р"),
                Arguments.of("Перевод от МАРИНА АЛЕКСАНДРОВНА Д. Банк Уралсиб + 600 ₽ - Баланс: 6 223,34 ₽ СЧЕТ • 7533"),
                Arguments.of("СЧЁТ0129 23:49 Перевод из Альфа-Банк +1000р от АНДРЕЙ Ф. Баланс: 1102.47р «Перевод денежных средств»")
        );
    }

}

