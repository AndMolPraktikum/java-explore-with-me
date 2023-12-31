package ru.practicum.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {

    private String status;              // Код статуса HTTP-ответа

    private String reason;              // Общее описание причины ошибки

    private String message;             // Сообщение об ошибке

    private LocalDateTime timestamp;    // Дата и время когда произошла ошибка (в формате "yyyy-MM-dd HH:mm:ss")
}
