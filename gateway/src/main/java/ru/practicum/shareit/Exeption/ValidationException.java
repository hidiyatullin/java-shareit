package ru.practicum.shareit.Exeption;

public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}