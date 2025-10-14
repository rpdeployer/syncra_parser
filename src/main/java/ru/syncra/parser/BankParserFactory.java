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
    private final SpitamenParser spitamenParser;
    private final AmeriaParser ameriaParser;
    private final SolidarnostParser solidarnostParser;
    private final IdramParser idramParser;
    private final MatinParser matinParser;
    private final M10Parser m10Parser;
    private final BirbankParser birbankParser;

    private final Map<BankType, BankParser> parsers = new HashMap<>();

    public BankParserFactory(
            SberParser sberParser,
            TinkoffParser tinkoffParser,
            OzonParser ozonParser,
            VaslParser vaslParser,
            AkbarsParser akbarsParser,
            SpitamenParser spitamenParser,
            AmeriaParser ameriaParser,
            SolidarnostParser solidarnostParser,
            IdramParser idramParser,
            MatinParser matinParser,
            M10Parser m10Parser,
            BirbankParser birbankParser

    ) {
        this.sberParser = sberParser;
        this.tinkoffParser = tinkoffParser;
        this.ozonParser = ozonParser;
        this.vaslParser = vaslParser;
        this.akbarsParser = akbarsParser;
        this.spitamenParser = spitamenParser;
        this.ameriaParser = ameriaParser;
        this.solidarnostParser = solidarnostParser;
        this.idramParser = idramParser;
        this.matinParser = matinParser;
        this.m10Parser = m10Parser;
        this.birbankParser = birbankParser;

        parsers.put(SBERBANK, sberParser);
        parsers.put(TBANK, tinkoffParser);
        parsers.put(OZON, ozonParser);
        parsers.put(VASL, vaslParser);
        parsers.put(AKBARS, akbarsParser);
        parsers.put(SPITAMEN, spitamenParser);
        parsers.put(AMERIA, ameriaParser);
        parsers.put(SOLIDARNOST, solidarnostParser);
        parsers.put(IDRAM, idramParser);
        parsers.put(MATIN, matinParser);
        parsers.put(M10, m10Parser);
        parsers.put(BIRBANK, birbankParser);
    }

    public BankParser getParser(BankType bankType) {
        return parsers.get(bankType);
    }
}

