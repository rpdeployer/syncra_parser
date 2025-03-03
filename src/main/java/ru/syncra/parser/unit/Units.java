package ru.syncra.parser.unit;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Units {

    public static final String UNKNOWN = "UNKNOWN";
    public static final String RUB = "RUB";

    private static final Map<Set<String>, String> units = new HashMap<>() {{
        put(Set.of("₽", "rub", "р"), RUB);
    }};

    public static String getUnit(String symbol) {
        if (symbol == null) {
            return UNKNOWN;
        }
        return units.entrySet().stream()
                .filter(entry -> entry.getKey().contains(symbol))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(UNKNOWN);
    }

}
