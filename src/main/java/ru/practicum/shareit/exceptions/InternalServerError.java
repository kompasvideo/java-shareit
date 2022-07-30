package ru.practicum.shareit.exceptions;

public class InternalServerError extends RuntimeException {
    public InternalServerError(final String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
