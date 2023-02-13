package ru.practicum.shareit.Exeption;

public class IncorrectUserOfItemException extends RuntimeException {
    public IncorrectUserOfItemException(String s) {
        super(s);
    }
}
