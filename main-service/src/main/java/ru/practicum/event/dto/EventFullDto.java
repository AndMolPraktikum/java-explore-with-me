package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.enums.State;
import ru.practicum.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {

    private String annotation;                  // + Аннотация

    private CategoryDto category;               // + Категория

    private long confirmedRequests;             ////// + Количество одобренных заявок на участие в данном событии

    private LocalDateTime createdOn;            // + Дата и время создания события

    private String description;                 // + Полное описание события

    private LocalDateTime eventDate;            // + Дата и время на которые намечено событие

    private long id;                            // + Идентификатор

    private UserShortDto initiator;             // + Создатель события

    private LocationDto location;               // + Широта и долгота места проведения события

    private boolean paid;                       // + Нужно ли оплачивать участие в событии

    private int participantLimit;               // + Ограничение на количество участников. Значение 0 - отсутствие ограничения

    private LocalDateTime publishedOn;          // + Дата и время публикации события

    private boolean requestModeration;          // + Нужна ли пре-модерация заявок на участие

    private State state;                        // + Список состояний жизненного цикла события

    private String title;                       // + Заголовок

    private long views;                         ////// + Количество просмотрев события

}
