package ru.syncra.parser;

import org.springframework.stereotype.Component;
import ru.syncra.entities.enums.BankType;
import ru.syncra.parser.bank.*;
import ru.syncra.parser.base.BankParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static ru.syncra.entities.enums.BankType.*;

@Component
public class BankParserFactory {

    private final SberParser sberParser;
    private final TinkoffParser tinkoffParser;
    private final OzonParser ozonParser;
    private final VaslParser vaslParser;
    private final AkbarsParser akbarsParser;

    private final Map<BankType, BankParser> parsers = new HashMap<>();

    public BankParserFactory(
            SberParser sberParser,
            TinkoffParser tinkoffParser,
            OzonParser ozonParser,
            VaslParser vaslParser,
            AkbarsParser akbarsParser
    ) {
        this.sberParser = sberParser;
        this.tinkoffParser = tinkoffParser;
        this.ozonParser = ozonParser;
        this.vaslParser = vaslParser;
        this.akbarsParser = akbarsParser;

        parsers.put(SBERBANK, sberParser);
        parsers.put(TBANK, tinkoffParser);
        parsers.put(OZON, ozonParser);
        parsers.put(VASL, vaslParser);
        parsers.put(AKBARS, akbarsParser);
    }

    public BankParser getParser(BankType bankType) {
        return parsers.get(bankType);
    }
}

