package ru.practicum.exception;

public class EventTimeConditionBadRequestException extends RuntimeException {

    public EventTimeConditionBadRequestException(String message) {
        super(message);
    }
}
