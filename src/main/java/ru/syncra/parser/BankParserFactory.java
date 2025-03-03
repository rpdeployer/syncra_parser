package ru.syncra.parser;

import org.springframework.stereotype.Component;
import ru.syncra.entities.enums.BankType;
import ru.syncra.parser.bank.SberParser;
import ru.syncra.parser.bank.TinkoffParser;
import ru.syncra.parser.base.BankParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static ru.syncra.entities.enums.BankType.SBERBANK;
import static ru.syncra.entities.enums.BankType.TBANK;

@Component
public class BankParserFactory {

    private final SberParser sberParser;
    private final TinkoffParser tinkoffParser;

    private final Map<BankType, BankParser> parsers = new HashMap<>();

    public BankParserFactory(SberParser sberParser, TinkoffParser tinkoffParser) {
        this.sberParser = sberParser;
        this.tinkoffParser = tinkoffParser;

        parsers.put(SBERBANK, sberParser);
        parsers.put(TBANK, tinkoffParser);
    }

    public BankParser getParser(BankType bankType) {
        return parsers.get(bankType);
    }
}

