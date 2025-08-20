package ru.syncra.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.syncra.entities.dto.ActiveApplication;
import ru.syncra.entities.dto.MessagePayload;
import ru.syncra.entities.dto.ParsedMessage;
import ru.syncra.entities.enums.BankType;
import ru.syncra.exception.BlockingException;
import ru.syncra.parser.base.BankParser;
import ru.syncra.parser.BankParserFactory;
import ru.syncra.service.core.CoreIntegrationService;
import ru.syncra.util.ApplicationUtils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageListener {

    private final BankParserFactory parserFactory;
    private final CoreIntegrationService coreIntegrationService;

    @RabbitListener(queues = "${spring.queues.message-queue}", containerFactory = "rabbitListenerContainerFactory")
    public void receiveMessage(MessagePayload message) {
        log.info("Обработка сообщения {}: {}", message.getFrom(), message.getMessage());

        CompletableFuture.runAsync(() -> {
            BankType bank = null;
            try {
                bank = BankType.getBankType(message.getFrom());

                BankParser parser = parserFactory.getParser(bank);

                parser.checkBlockingMessage(message.getMessage());
                ParsedMessage parsedMessage = parser.parse(message.getMessage(), message.getIsSms());
                log.info("[{}] Спарсеное сообщение: {}", message.getDeviceId(), parsedMessage);

                if (parsedMessage != null) {
                    List<ActiveApplication> activeApplications = coreIntegrationService.getActiveApplications(bank, message.getDeviceId());

                    ActiveApplication activeApplication = ApplicationUtils.findApplication(activeApplications, message, parsedMessage);
                    if (Optional.ofNullable(activeApplication).isPresent()) {
                        log.info("[{}] Найдена заявка, подтверждаем: {}", message.getDeviceId(), activeApplication);
                        coreIntegrationService.confirmApplication(activeApplication.getId());
                    } else {
                        log.warn("[{}] Не найдено ни одной заявки, отправляю репорт!", message.getDeviceId());
//                        coreIntegrationService.reportNotFound(message);
                    }
                } else {
                    log.warn("[{}] Не найдено совпадений при парсинге: {}", message.getDeviceId(), message);
                }
            } catch (BlockingException e) {
                log.warn("[{}] Применяем санкцию 5 к сообщению: {}", message.getDeviceId(), message);
                coreIntegrationService.blockDevice(message.getDeviceId(), bank.getBankIdCore());
            } catch (Exception e) {
                log.error("[{}] Ошибка при парсинге сообщения: {}", message.getDeviceId(), message, e);
            }
        });
    }

}

