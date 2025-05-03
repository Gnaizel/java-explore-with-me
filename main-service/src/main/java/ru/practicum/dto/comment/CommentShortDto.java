package ru.practicum.dto.comment;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CommentShortDto {
    private String comment;
    private String author;
}
