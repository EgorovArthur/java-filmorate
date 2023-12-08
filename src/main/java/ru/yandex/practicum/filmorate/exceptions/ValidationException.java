package ru.yandex.practicum.filmorate.exceptions;

public class ValidationException extends IllegalArgumentException {
    public ValidationException() {
    }

    public ValidationException(String s) {
        super(s);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
