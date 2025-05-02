package ru.practicum.exception;

public class RequestNotExistException extends RuntimeException {
    public RequestNotExistException(String message) {
        super(message);
    }
}
