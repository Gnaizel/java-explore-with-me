package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.comment.CommentShortDto;
import ru.practicum.model.Comment;

@Component
public class CommentMapper {
    public static CommentShortDto toShortDto(Comment comment) {
        CommentShortDto shortDto = CommentShortDto.builder()
                .comment(comment.getComment())
                .author(comment.getUser().getId())
                .id(comment.getId())
                .build();
        return shortDto;
    }
}
