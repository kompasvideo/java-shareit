package ru.practicum.shareit.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(final String message) {
        super(message);
    }
    public BadRequestException() {
        super();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
