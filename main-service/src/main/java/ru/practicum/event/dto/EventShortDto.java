package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {

    private String annotation;                  // + Аннотация

    private CategoryDto category;               // + Категория

    private long confirmedRequests;              ////// + Количество одобренных заявок на участие в данном событии

    private LocalDateTime eventDate;            // + Дата и время на которые намечено событие

    private long id;                            // + Идентификатор

    private UserShortDto initiator;             // + Создатель события

    private boolean paid;                       // + Нужно ли оплачивать участие в событии

    private String title;                       // + Заголовок

    private long views;                      ////// + Количество просмотрев события

    public EventShortDto(String annotation, CategoryDto category, LocalDateTime eventDate, long id,
                         UserShortDto initiator, boolean paid, String title) {
        this.annotation = annotation;
        this.category = category;
        this.eventDate = eventDate;
        this.id = id;
        this.initiator = initiator;
        this.paid = paid;
        this.title = title;
    }
}
