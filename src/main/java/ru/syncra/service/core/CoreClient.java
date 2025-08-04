package ru.syncra.service.core;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.syncra.entities.dto.*;

import java.util.List;

@FeignClient(name = "core-service", url = "${core.service.url}")
public interface CoreClient {

    @GetMapping("/api/parser/active")
    ApiResponse<List<ActiveApplication>> getActiveApplications(
            @RequestParam String bank,
            @RequestParam String deviceId
    );

    @PostMapping("/api/parser/confirm")
    ApiResponse<Void> confirmApplication(@RequestBody ConfirmPayload body);

    @PostMapping("/api/parser/report")
    ApiResponse<Void> reportUnmatchedTransaction(@RequestBody MessagePayload body);

    @PostMapping("/api/mobile/block")
    ApiResponse<Void> blockDeviceAndRequisit(@RequestBody BlockPayload body);

}

