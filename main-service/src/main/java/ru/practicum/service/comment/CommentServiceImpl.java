package ru.practicum.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.comment.CommentCreateDto;
import ru.practicum.dto.comment.CommentShortDto;
import ru.practicum.exception.CommentNotExist;
import ru.practicum.exception.CommentValidationError;
import ru.practicum.exception.EventNotExistException;
import ru.practicum.exception.UserNotExistException;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.User;
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public List<CommentShortDto> createComment(Long eventId, Long userId, CommentCreateDto commentCreateDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotExistException("User not found"));

        Comment comment = Comment.builder()
                .comment(commentCreateDto.getComment())
                .event(eventId)
                .user(user)
                .build();
        commentRepository.save(comment);
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotExistException("Event not found"));
        return event.getComments().stream().map(CommentMapper::toShortDto).toList();
    }

    @Override
    public List<CommentShortDto> getCommentByEventId(Long eventId) {
        return List.of();
    }

    @Override
    public List<CommentShortDto> updateComment(Long userId, Long commentId, CommentCreateDto commentCreateDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotExistException("User not found"));
        Comment firstComment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotExist("Comment not found"));

        Comment comment = Comment.builder()
                .comment(commentCreateDto.getComment())
                .event(firstComment.getEvent())
                .id(firstComment.getId())
                .user(user)
                .build();
        if (firstComment.equals(comment)) throw new CommentValidationError("comment identical");
        commentRepository.save(comment);

        Event event = eventRepository.findById(comment.getEvent()).orElseThrow(() -> new EventNotExistException("Event not found"));

        return event.getComments().stream().map(CommentMapper::toShortDto).toList();
    }

    @Override
    public List<CommentShortDto> deleteComment(Long userId, Long commentId) {
        Comment firstComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotExist("Comment not found"));

        if (!firstComment.getUser().getId().equals(userId)) {
            throw new CommentValidationError("You're not Owner");
        }

        commentRepository.deleteById(commentId);
        Event event = eventRepository.findById(firstComment.getEvent())
                .orElseThrow(() -> new EventNotExistException("Event not found"));

        return event.getComments().stream()
                .map(CommentMapper::toShortDto)
                .toList();
    }
}
