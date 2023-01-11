package ru.practicum.shareit.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            MissingRequestHeaderException.class,
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("{} : {}", e.getClass().getSimpleName(), e.getMessage());
        return "Validation error";
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("{} : {}", e.getClass().getSimpleName(), e.getMessage());
        return "Validation error";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleInternalException(Exception e) {
        log.error("{} : {}", e.getClass().getSimpleName(), e.getMessage());
        return "Internal error";
    }
}
