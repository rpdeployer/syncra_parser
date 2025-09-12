package ru.syncra;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.syncra.entities.dto.ActiveApplication;
import ru.syncra.entities.dto.MessagePayload;
import ru.syncra.entities.dto.ParsedMessage;
import ru.syncra.entities.enums.BankType;
import ru.syncra.listener.MessageListener;
import ru.syncra.parser.BankParserFactory;
import ru.syncra.parser.base.BankParser;
import ru.syncra.producer.MessageUpdateProducer;
import ru.syncra.service.core.CoreIntegrationService;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageListenerTest {

    @Mock
    private BankParserFactory parserFactory;

    @Mock
    private BankParser bankParser;

    @Mock
    private CoreIntegrationService coreIntegrationService;

    @Mock
    private MessageUpdateProducer messageUpdateProducer;

    @InjectMocks
    private MessageListener messageListener;

    private MessagePayload message;

    @BeforeEach
    void setUp() throws Exception {
        message = new MessagePayload();
        message.setMessageId(UUID.randomUUID().toString());
        message.setFrom("Ozon Банк");
        message.setTo("Неизвестный номер");
        message.setTimestamp("1757668360437");
        message.setMessage("Пополнение через СБП на 3 098 ₽. Олег Романович Ф. Сбербанк. Баланс 3 098 ₽");
        message.setIsSms(false);
        message.setDeviceId("device123");

        when(parserFactory.getParser(any(BankType.class)))
                .thenReturn(bankParser);
        when(bankParser.parse(anyString(), anyBoolean()))
                .thenReturn(new ParsedMessage("RUB", "3098"));
    }

    @Test
    void testReceiveMessage_FindsActiveApplication() throws Exception {
        // given
        ActiveApplication app = new ActiveApplication();
        app.setId(UUID.randomUUID().toString());
        app.setFrom("2025-09-12T09:11:15.084467Z");
        app.setTo("2025-09-12T09:21:15.084475Z");
        app.setAmount(new java.math.BigDecimal("3098.00000000"));

        when(coreIntegrationService.getActiveApplications(any(), any()))
                .thenReturn(List.of(app));
        doNothing().when(coreIntegrationService)
                .confirmApplication(anyString(), anyString());

        // when
        messageListener.receiveMessage(message);

        // then
        verify(coreIntegrationService, timeout(2000))
                .confirmApplication(eq(app.getId()), eq(message.getMessageId()));
    }

    @Test
    void testReceiveMessage_NoActiveApplications() throws Exception {
        // given
        when(coreIntegrationService.getActiveApplications(any(), any()))
                .thenReturn(List.of());

        // when
        messageListener.receiveMessage(message);

        // then
        verify(coreIntegrationService, never())
                .confirmApplication(any(), any());
    }
}

