package ru.practicum.shareit.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(final String message) {
        super(message);
    }

    public NotFoundException() {
        super();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
