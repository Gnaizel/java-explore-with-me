package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.model.Comment;

@Component
public class CommentMapper {
    public static CommentDto toShortDto(Comment comment) {
        return CommentDto.builder()
                .comment(comment.getComment())
                .author(comment.getUser().getId())
                .id(comment.getId())
                .build();
    }
}
