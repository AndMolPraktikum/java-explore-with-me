package ru.practicum.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.user.dto.UserShortDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentShortDto {

    private Long id;

    private String header;

    private String description;

    private UserShortDto author;

    private Long event;
}
