package ru.practicum.shareit.Exeption;

public class IncorrectStatusOfItemException extends RuntimeException {
    public IncorrectStatusOfItemException(String s) {
        super(s);
    }
}
