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
            try {
                BankType bank = BankType.getBankType(message.getFrom());

                BankParser parser = parserFactory.getParser(bank);

                parser.checkBlockingMessage(message.getMessage());
                ParsedMessage parsedMessage = parser.parse(message.getMessage(), message.getIsSms());
                log.info("Спарсеное сообщение: {}", parsedMessage);

                if (parsedMessage != null) {
                    List<ActiveApplication> activeApplications = coreIntegrationService.getActiveApplications(bank, message.getDeviceId());

                    ActiveApplication activeApplication = ApplicationUtils.findApplication(activeApplications, message, parsedMessage);
                    if (Optional.ofNullable(activeApplication).isPresent()) {
                        log.info("Найдена заявка, подтверждаем: {}", activeApplication);
                        coreIntegrationService.confirmApplication(activeApplication.getId());
                    } else {
                        log.warn("Не найдено ни одной заявки, отправляю репорт!");
                        coreIntegrationService.reportNotFound(message);
                    }
                } else {
                    log.warn("Не найдено совпадений при парсинге: {}", message);
                }
            } catch (BlockingException e) {
                log.warn("Применяем санкцию 5 к сообщению: {}", message);
                coreIntegrationService.blockDevice(message.getDeviceId());
            } catch (Exception e) {
                log.error("Ошибка при парсинге сообщения: {}", message, e);
            }
        });
    }

}

