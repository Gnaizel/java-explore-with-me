package ru.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TimeValidationError extends RuntimeException {
    public TimeValidationError(String message) {
        super(message);
    }
}
