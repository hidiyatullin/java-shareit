package ru.practicum.shareit.Exeption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleThrowable(final MethodArgumentNotValidException e) {
        log.info("400 {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> userNotFound(final UserNotFoundException e) {
        log.info("Пользователь не был найден, {}", e.getMessage());
        return Map.of(
                "error", "404",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> incorrectCount(final IncorrectEmailException e) {
        log.debug("409, {}", e.getMessage());
        return Map.of(
                "error", "Ошибка с параметром count.",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> itemNotFound(final ItemNotFoundException e) {
        log.info("Вещь не была найдена, {}", e.getMessage());
        return Map.of(
                "error", "404",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> incorrectUserOfItem(final IncorrectUserOfItemException e) {
        log.debug("404, {}", e.getMessage());
        return Map.of(
                "error", "Ошибка с параметром user.",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> incorrectStatusOfItem(final IncorrectStatusOfItemException e) {
        log.debug("400, {}", e.getMessage());
        return Map.of(
                "error", "Ошибка с параметром available.",
                "errorMessage", e.getMessage()
        );
    }
}

