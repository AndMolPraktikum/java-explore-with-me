package ru.practicum.comments.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {

    @NotNull
    @Size(min = 1, max = 50)
    private String header;

    @NotNull
    @Size(min = 1, max = 1900)
    private String description;
}
