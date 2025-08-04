package ru.syncra.service.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.syncra.entities.dto.*;
import ru.syncra.entities.enums.BankType;
import ru.syncra.util.HmacSigner;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoreIntegrationService {

    private final CoreClient coreClient;

    public List<ActiveApplication> getActiveApplications(BankType bankType, String deviceId) {
        ApiResponse<List<ActiveApplication>> response = coreClient.getActiveApplications(bankType.name(), deviceId);
        return extractValue(response, "getActiveApplications", bankType.name(), deviceId);
    }

    public void confirmApplication(Long id) {
        ApiResponse<Void> response = coreClient.confirmApplication(new ConfirmPayload(id.toString(), HmacSigner.SECRET));
        extractValue(response, "confirmApplication", id);
    }

    public void reportNotFound(MessagePayload messagePayload) {
        ApiResponse<Void> response = coreClient.reportUnmatchedTransaction(messagePayload);
        extractValue(response, "reportNotFound", messagePayload);
    }

    public void blockDevice(String deviceId, String bankIdCore) {
        ApiResponse<Void> response = coreClient.blockDeviceAndRequisit(new BlockPayload(deviceId, bankIdCore, HmacSigner.SECRET));
        extractValue(response, "blockDevice", deviceId);
    }

    private <T> T extractValue(ApiResponse<T> response, String methodName, Object... params) {
        if (response.isFailure() | !response.isSuccess()) {
            log.error("Ошибка в методе {} с параметрами {}. Ошибки: {}", methodName, params, response.getFailures());
        }

        return response.getValue();
    }
}

