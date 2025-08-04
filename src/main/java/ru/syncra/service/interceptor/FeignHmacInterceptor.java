package ru.syncra.service.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.syncra.util.HmacSigner;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Component
public class FeignHmacInterceptor implements RequestInterceptor {

    private String payloadTemplate = "%s;%s;%s";

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void apply(RequestTemplate template) {
        String path = template.path();
        String timestamp = String.valueOf(Instant.now().getEpochSecond());

        try {
            String body = template.body() != null
                    ? new String(template.body(), StandardCharsets.UTF_8)
                    : null;

            String signature = generateSignature(path, timestamp, body);
            template.header("Signature", signature);

        } catch (Exception e) {
            throw new RuntimeException("HMAC signing failed", e);
        }
    }

    private String generateSignature(String path, String timestamp, String body) throws Exception {
        if (path.startsWith("/api/applications/active")) {
            return HmacSigner.generateSignature(timestamp);
        }

        if (body == null || body.isEmpty()) return HmacSigner.generateSignature(timestamp);

        Map<String, Object> json = mapper.readValue(body, Map.class);
        String salt = (String) json.get("salt");

        if (path.startsWith("/api/mobile/block") || path.startsWith("/api/applications/report")) {
            String deviceId = (String) json.get("deviceId");
            return HmacSigner.generateSignature(payloadTemplate.formatted(timestamp, deviceId, salt));
        }

        if (path.startsWith("/api/applications/confirm")) {
            String paymentId = (String) json.get("paymentId");
            return HmacSigner.generateSignature(payloadTemplate.formatted(timestamp, paymentId, salt));
        }

        return HmacSigner.generateSignature(timestamp);
    }
}

