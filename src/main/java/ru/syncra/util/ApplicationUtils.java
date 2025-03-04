package ru.syncra.util;

import lombok.experimental.UtilityClass;
import ru.syncra.entities.dto.ActiveApplication;
import ru.syncra.entities.dto.MessagePayload;
import ru.syncra.entities.dto.ParsedMessage;
import ru.syncra.exception.ParserException;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static java.lang.Long.parseLong;

@UtilityClass
public class ApplicationUtils {

    public static ActiveApplication findApplication(List<ActiveApplication> activeApplications, MessagePayload message, ParsedMessage parsedMessage) throws Exception {
        long timestamp = parseLong(message.getTimestamp());
        BigDecimal messageAmount = BigDecimal.valueOf(Double.parseDouble(parsedMessage.getAmount()));

        return activeApplications.stream()
//                .filter(app -> isWithinTimeRange(timestamp, app.getFrom(), app.getTo()))
                .filter(app -> app.getAmount().compareTo(messageAmount) == 0)
                .findFirst()
                .orElse(null);
    }

    private static boolean isWithinTimeRange(long timestamp, String from, String to) {
        long fromTime = parseLong(from);
        long toTime = parseLong(to);
        return timestamp >= fromTime && timestamp <= toTime;
    }

}
