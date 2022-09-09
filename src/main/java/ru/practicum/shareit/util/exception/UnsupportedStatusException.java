package ru.practicum.shareit.util.exception;


import lombok.Getter;

public class UnsupportedStatusException extends RuntimeException {

    @Getter
    public String unknownStatus;

    public UnsupportedStatusException(String unknownStatus) {
        super();
        this.unknownStatus = unknownStatus;
    }
}
