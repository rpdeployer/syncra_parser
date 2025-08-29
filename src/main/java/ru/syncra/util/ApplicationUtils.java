package ru.syncra.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.syncra.entities.dto.ActiveApplication;
import ru.syncra.entities.dto.MessagePayload;
import ru.syncra.entities.dto.ParsedMessage;
import ru.syncra.exception.ParserException;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import static java.lang.Long.parseLong;

@Slf4j
@UtilityClass
public class ApplicationUtils {

    public static ActiveApplication findApplication(List<ActiveApplication> activeApplications, MessagePayload message, ParsedMessage parsedMessage) throws Exception {
        long timestamp = parseLong(message.getTimestamp());
        BigDecimal messageAmount = BigDecimal.valueOf(Double.parseDouble(parsedMessage.getAmount()));

        return activeApplications.stream()
                .filter(app -> isWithinTimeRange(timestamp, app.getFrom(), app.getTo()))
                .filter(app -> app.getAmount().compareTo(messageAmount) == 0)
                .findFirst()
                .orElse(null);
    }

    public static boolean isWithinTimeRange(long timestamp, String from, String to) {
        try {
            long fromTime = Instant.parse(from).toEpochMilli();
            long toTime = Instant.parse(to).toEpochMilli();

            var ts = timestamp * 1000;

            return ts >= fromTime && ts <= toTime;
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format: " + e.getMessage());
            return false;
        }
    }

}
