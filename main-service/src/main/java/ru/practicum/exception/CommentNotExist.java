package ru.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CommentNotExist extends RuntimeException {
    public CommentNotExist(String message) {
        super(message);
    }
}
