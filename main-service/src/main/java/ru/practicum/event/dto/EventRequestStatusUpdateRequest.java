package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.enums.Status;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateRequest {  // Изменение статуса запроса на участие в событии текущего пользователя

    private List<Long> requestIds;           // Идентификаторы запросов на участие в событии текущего пользователя

    private Status status;                      // Новый статус запроса на участие в событии текущего пользователя
}
