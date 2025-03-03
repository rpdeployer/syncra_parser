package ru.syncra.exception;

public class BlockingException extends Exception {

    public BlockingException() {
        super("Пришло уведовление попадающие под штрафные санкции, отключение устройства");
    }

}
