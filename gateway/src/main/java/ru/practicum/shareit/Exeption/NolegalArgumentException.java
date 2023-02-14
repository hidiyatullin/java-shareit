package ru.practicum.shareit.Exeption;

public class NolegalArgumentException extends IllegalArgumentException {

    public NolegalArgumentException(String error) {
        super(error);
    }
}