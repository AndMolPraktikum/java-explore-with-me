package ru.practicum.event.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.category.model.Category;
import ru.practicum.enums.State;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;                        //Идентификатор

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "initiator_id")
    private User initiator;                 //Создатель события

    private String annotation;              //Аннотация

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "category_id")
    private Category category;              //Категория

    @Column(name = "created_on")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdOn;        //Дата и время создания события

    private String description;             //Полное описание события

    @Column(name = "event_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;        //Дата и время на которые намечено событие

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "location_id")
    private Location location;              //Широта и долгота места проведения события

    private boolean paid;                   //Нужно ли оплачивать участие в событии

    @Column(name = "participant_limit")
    private int participantLimit;           //Ограничение на количество участников. Значение 0 - отсутствие ограничения

    @Column(name = "published_on")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;      //Дата и время публикации события

    @Column(name = "request_moderation")
    private boolean requestModeration;      //Нужна ли пре-модерация заявок на участие

    @Enumerated(EnumType.STRING)
    private State state;                    //Список состояний жизненного цикла события

    private String title;                   //Заголовок

    public Event(String annotation, String description,
                 LocalDateTime eventDate, Location location, boolean paid, int participantLimit,
                 boolean requestModeration, State state, String title) {
        this.annotation = annotation;
        this.description = description;
        this.eventDate = eventDate;
        this.location = location;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.requestModeration = requestModeration;
        this.state = state;
        this.title = title;
    }
}
