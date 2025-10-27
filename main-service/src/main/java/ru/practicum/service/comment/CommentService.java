package ru.practicum.service.comment;

import ru.practicum.dto.comment.CommentCreateDto;
import ru.practicum.dto.comment.CommentDto;

import java.util.List;

public interface CommentService {

    List<CommentDto> getCommentsByEventId(Long eventId);

    CommentDto createComment(Long eventId,
                             Long userId,
                             CommentCreateDto commentCreateDto);

    CommentDto updateComment(Long userId,
                             Long commentId,
                             CommentCreateDto commentCreateDto);

    void deleteComment(Long userId,
                                        Long commentId);
}
