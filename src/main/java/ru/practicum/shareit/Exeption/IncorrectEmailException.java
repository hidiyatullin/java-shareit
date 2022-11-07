package ru.practicum.shareit.Exeption;

public class IncorrectEmailException extends RuntimeException {
    public IncorrectEmailException(String s) {
        super(s);
    }
}
