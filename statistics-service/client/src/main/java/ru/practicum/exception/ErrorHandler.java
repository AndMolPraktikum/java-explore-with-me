package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(StatisticWrongTimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleStatisticWrongTimeException(final StatisticWrongTimeException e) {
        log.info("400 {}", e.getMessage(), e);
        return new Response(String.format("%s %s", LocalDateTime.now(), e.getMessage()));
    }
}
