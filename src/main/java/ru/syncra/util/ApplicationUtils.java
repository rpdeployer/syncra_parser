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
import java.time.temporal.ChronoUnit;
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
                .filter(app -> {
                    BigDecimal lowerBound = app.getAmount().subtract(app.getRange());
                    BigDecimal upperBound = app.getAmount().add(app.getRange());
                    return messageAmount.compareTo(lowerBound) >= 0
                            && messageAmount.compareTo(upperBound) <= 0;
                })
                .findFirst()
                .orElse(null);
    }

    public static boolean isWithinTimeRange(long timestamp, String from, String to) {
        try {
            Instant fromInstant = Instant.parse(from).truncatedTo(ChronoUnit.MILLIS);
            Instant toInstant = Instant.parse(to).truncatedTo(ChronoUnit.MILLIS);
            Instant ts = Instant.ofEpochMilli(timestamp);

            return !ts.isBefore(fromInstant) && !ts.isAfter(toInstant);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format: " + e.getMessage());
            return false;
        }
    }

}
