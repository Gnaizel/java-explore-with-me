package ru.practicum.exception;

public class RequestAlreadyExistException extends RuntimeException {
    public RequestAlreadyExistException(String message) {
        super(message);
    }
}
