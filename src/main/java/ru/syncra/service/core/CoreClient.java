package ru.syncra.service.core;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.syncra.Application;
import ru.syncra.entities.dto.*;

import java.util.List;

@FeignClient(name = "core-service", url = "${core.service.url}")
public interface CoreClient {

    @GetMapping("/api/applications/active")
    List<ActiveApplication> getActiveApplications(@RequestParam String bank, @RequestParam String deviceId);

    @PostMapping("/api/applications/confirm")
    void confirmApplication(@RequestBody ConfirmPayload body);

    @PostMapping("/api/applications/report")
    void reportUnmatchedTransaction(@RequestBody MessagePayload body);

    @PostMapping("/api/mobile/block")
    void blockDevice(@RequestBody BlockPayload body);

}

