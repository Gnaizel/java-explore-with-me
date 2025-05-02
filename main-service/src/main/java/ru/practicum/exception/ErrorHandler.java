package ru.practicum.exception;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.model.Error;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
public class ErrorHandler {
    private final DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public Error handleUserNameAlreadyExistException(final NameAlreadyExistException exception) {
        return new Error(exception.getMessage(), "Integrity constraint has been violated.",
                HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(formater));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public Error handleUserNameAlreadyExistException(final CommentConflictException exception) {
        return new Error(exception.getMessage(), "Integrity constraint has been violated.",
                HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(formater));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public Error handleRequestAlreadyExistException(final RequestAlreadyExistException exception) {
        return new Error(exception.getMessage(), "Integrity constraint has been violated.",
                HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(formater));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public Error handleCategoryIsNotEmptyException(final CategoryIsNotEmptyException exception) {
        return new Error(exception.getMessage(), "For the requested operation the conditions are not met.",
                HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(formater));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public Error handleEventIsNotPublishedException(final EventIsNotPublishedException exception) {
        return new Error(exception.getMessage(), "For the requested operation the conditions are not met.",
                HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(formater));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public Error handleWrongUserException(final WrongUserException exception) {
        return new Error(exception.getMessage(), "For the requested operation the conditions are not met.",
                HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(formater));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public Error handleAlreadyPublishedException(final AlreadyPublishedException exception) {
        return new Error(exception.getMessage(), "For the requested operation the conditions are not met.",
                HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(formater));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public Error handleParticipantLimitException(final ParticipantLimitException exception) {
        return new Error(exception.getMessage(), "For the requested operation the conditions are not met.",
                HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(formater));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public Error handleEventAlreadyCanceledException(final EventAlreadyCanceledException exception) {
        return new Error(exception.getMessage(), "For the requested operation the conditions are not met.",
                HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(formater));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public Error handleRequestAlreadyConfirmedException(final RequestAlreadyConfirmedException exception) {
        return new Error(exception.getMessage(), "For the requested operation the conditions are not met.",
                HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(formater));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Error handleWrongTimeOfEventException(final WrongTimeException exception) {
        return new Error(exception.getMessage(), "For the requested operation the conditions are not met.",
                HttpStatus.FORBIDDEN.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(formater));
    }

    @ExceptionHandler
    @ResponseBody
    public Error handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException exception) {
        return new Error(exception.getMessage(), "Incorrectly made request.",
                HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(formater));
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        return new Error(exception.getMessage(), "Incorrectly made request.",
                HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(formater));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Error handleUserNotExistException(final UserNotExistException exception) {
        return new Error("Can't delete user with this id", "User with this id doesn't exist",
                HttpStatus.NOT_FOUND.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(formater));
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Error handleEmptyResultDataAccessException(final EmptyResultDataAccessException exception) {
        return new Error("It is impossible to do the operation", "data not found",
                HttpStatus.NOT_FOUND.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(formater));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Error handleCompilationNotExistException(final CompilationNotExistException exception) {
        return new Error("Can't delete user with this id", "User with this id doesn't exist",
                HttpStatus.NOT_FOUND.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(formater));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Error handleRequestNotExistException(final RequestNotExistException exception) {
        return new Error(exception.getMessage(), "Request with this id doesn't exist",
                HttpStatus.NOT_FOUND.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(formater));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Error handleEventNotExistException(final EventNotExistException exception) {
        return new Error(exception.getMessage(), "Event with this id doesn't exist",
                HttpStatus.NOT_FOUND.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(formater));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleCategoryNotExistException(final CategoryNotExistException exception) {
        return new Error("Can't delete category with this id", "Category with this id doesn't exist",
                HttpStatus.NOT_FOUND.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(formater));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleCommentNotExistException(final CommentNotExistException exception) {
        return new Error(exception.getMessage(), "Category with this id doesn't exist",
                HttpStatus.NOT_FOUND.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(formater));
    }
}
