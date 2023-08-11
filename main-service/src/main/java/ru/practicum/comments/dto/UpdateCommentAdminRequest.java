package ru.practicum.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.enums.CommentStatus;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCommentAdminRequest {

    @NotNull
    private Long commentId;

    @NotNull
    private CommentStatus commentStatus;

    private String adminComment;
}
