package ru.practicum.comments.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.enums.CommentStatus;
import ru.practicum.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentFullDto {

    private Long id;

    private String header;

    private String description;

    private LocalDateTime created;

    private LocalDateTime published;

    private UserShortDto author;

    private Long event;

    private CommentStatus commentStatus;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String adminComment;
}
