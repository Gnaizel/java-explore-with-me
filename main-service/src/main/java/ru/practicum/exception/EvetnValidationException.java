package ru.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EvetnValidationException extends RuntimeException {
    public EvetnValidationException(String message) {
        super(message);
    }
}
