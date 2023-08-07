package ru.practicum.exception;

public class UserParticipationNotFoundException extends RuntimeException {

    public UserParticipationNotFoundException(String message) {
        super(message);
    }
}
