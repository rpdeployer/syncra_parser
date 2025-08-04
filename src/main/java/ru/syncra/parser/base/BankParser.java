package ru.syncra.parser.base;

import ru.syncra.entities.dto.ParsedMessage;
import ru.syncra.exception.BlockingException;

public interface BankParser {

    ParsedMessage parse(String text, boolean isSms);

    void checkBlockingMessage(String text) throws BlockingException;

}

