package ru.practicum.service.comment;

import ru.practicum.dto.comment.CommentCreateDto;
import ru.practicum.dto.comment.CommentShortDto;

import java.util.List;

public interface CommentService {

    List<CommentShortDto> getCommentByEventId(Long eventId);

    List<CommentShortDto> createComment(Long eventId,
                                        Long userId,
                                        CommentCreateDto commentCreateDto);

    List<CommentShortDto> updateComment(Long userId,
                                        Long commentId,
                                        CommentCreateDto commentCreateDto);

    List<CommentShortDto> deleteComment(Long userId,
                                        Long commentId);
}
