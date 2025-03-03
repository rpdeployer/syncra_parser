package ru.syncra.service.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.syncra.entities.dto.*;
import ru.syncra.entities.enums.BankType;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoreIntegrationService {

    private final CoreClient coreClient;

    public List<ActiveApplication> getActiveApplications(BankType bankType, String deviceId) {
        return coreClient.getActiveApplications(bankType.name(), deviceId);
    }

    public void confirmApplication(Long id) {
        coreClient.confirmApplication(new ConfirmPayload(id.toString()));
    }

    public void reportNotFound(MessagePayload messagePayload) {
        coreClient.reportUnmatchedTransaction(messagePayload);
    }

    public void blockDevice(String deviceId) {
        coreClient.blockDevice(new BlockPayload(deviceId));
    }

}

