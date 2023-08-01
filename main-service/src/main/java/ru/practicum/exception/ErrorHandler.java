package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {


    //    @ExceptionHandler
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public Response handleItemIsUnavailableException(final ItemIsUnavailableException e) {
//        log.info("400 {}", e.getMessage(), e);
//        return new Response(String.format("%s %s", LocalDateTime.now(), e.getMessage()));
//    }
//
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleEventTimeConditionBadRequestException(final EventTimeConditionBadRequestException e) {
        log.info("400 {}", e.getMessage(), e);
        return new ApiError("BAD_REQUEST",
                "Incorrectly made request.",
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.info("400 {}", e.getMessage(), e);
        return new ApiError("BAD_REQUEST",
                "Incorrectly made request.",
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMissingServletRequestParameterException(final MissingServletRequestParameterException e) {
        log.info("400 {}", e.getMessage(), e);
        return new ApiError("BAD_REQUEST",
                "Incorrectly made request.",
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleUserNotFoundException(final UserNotFoundException e) {
        log.info("404 {}", e.getMessage(), e);
        return new ApiError("NOT_FOUND",
                "The required object was not found.",
                e.getMessage(),
                LocalDateTime.now());
    }
//
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public Response handleUserHaveNoSuchItemException(final UserHaveNoSuchItemException e) {
//        log.info("404 {}", e.getMessage(), e);
//        return new Response(String.format("%s %s", LocalDateTime.now(), e.getMessage()));
//    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleCompilationNotFoundException(final CompilationNotFoundException e) {
        log.info("404 {}", e.getMessage(), e);
        return new ApiError("NOT_FOUND",
                "The required object was not found.",
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleRequestNotFoundException(final RequestNotFoundException e) {
        log.info("404 {}", e.getMessage(), e);
        return new ApiError("NOT_FOUND",
                "The required object was not found.",
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleEventNotFoundException(final EventNotFoundException e) {
        log.info("404 {}", e.getMessage(), e);
        return new ApiError("NOT_FOUND",
                "The required object was not found.",
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleCategoryNotFoundException(final CategoryNotFoundException e) {
        log.info("404 {}", e.getMessage(), e);
        return new ApiError("NOT_FOUND",
                "The required object was not found.",
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleEventUpdateConditionsConflictException(final EventUpdateConditionsConflictException e) {
        log.info("409 {}", e.getMessage(), e);
        return new ApiError("FORBIDDEN",
                "For the requested operation the conditions are not met.",
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleEventDateForbiddenException(final EventConditionForbiddenException e) {
        log.info("409 {}", e.getMessage(), e);
        return new ApiError("FORBIDDEN",
                "For the requested operation the conditions are not met.",
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleRequestCreationConditionsException(final RequestCreationConditionsException e) {
        log.info("409 {}", e.getMessage(), e);
        return new ApiError("FORBIDDEN",
                "For the requested operation the conditions are not met.",
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        log.info("409 {}", e.getMessage(), e);
        return new ApiError("CONFLICT",
                "For the requested operation the conditions are not met.",
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataCategoryUsedConflictException(final CategoryUsedConflictException e) {
        log.info("409 {}", e.getMessage(), e);
        return new ApiError("CONFLICT",
                "For the requested operation the conditions are not met.",
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response handleThrowable(final Throwable e) {
        log.info("500 {}", e.getMessage(), e);
        return new Response(String.format("%s %s", LocalDateTime.now(), e.getMessage()));
    }
}
