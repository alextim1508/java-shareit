package ru.practicum.shareit.util.exception;


public class UnsupportedStateException extends RuntimeException {

    public String unknownStatus;

    public UnsupportedStateException(String unknownStatus) {
        super();
        this.unknownStatus = unknownStatus;
    }
}
