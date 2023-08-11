package ru.practicum.exception;

public class CommentStatusConflictException extends RuntimeException {

    public CommentStatusConflictException(String message) {
        super(message);
    }
}
