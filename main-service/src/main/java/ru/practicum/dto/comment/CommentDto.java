package ru.practicum.dto.comment;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CommentDto {
    private String comment;
    private long author;
    private long id;
}
