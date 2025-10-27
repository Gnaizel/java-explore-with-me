package ru.practicum.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.comment.CommentCreateDto;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.exception.CommentNotExist;
import ru.practicum.exception.CommentValidationError;
import ru.practicum.exception.EventNotExistException;
import ru.practicum.exception.UserNotExistException;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.model.Comment;
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
    public CommentDto createComment(Long eventId, Long userId, CommentCreateDto commentCreateDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotExistException("User not found"));

        Comment comment = Comment.builder()
                .comment(commentCreateDto.getComment())
                .event(eventRepository
                        .findById(eventId)
                        .orElseThrow(() -> new EventNotExistException("Event not found")))
                .user(user)
                .build();
        return CommentMapper.toShortDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentDto> getCommentsByEventId(Long eventId) {
        return commentRepository.findAllByEventId(eventId).stream().map(CommentMapper::toShortDto).toList();
    }

    @Override
    public CommentDto updateComment(Long userId, Long commentId, CommentCreateDto commentCreateDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotExistException("User not found"));
        Comment firstComment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotExist("Comment not found"));

        Comment comment = Comment.builder()
                .comment(commentCreateDto.getComment())
                .event(firstComment.getEvent())
                .id(firstComment.getId())
                .user(user)
                .build();
        if (firstComment.equals(comment)) throw new CommentValidationError("comment identical");

        return CommentMapper.toShortDto(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(Long userId, Long commentId) {
        Comment firstComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotExist("Comment not found"));

        if (!firstComment.getUser().getId().equals(userId)) {
            throw new CommentValidationError("You're not Owner");
        }

        commentRepository.deleteById(commentId);
        eventRepository.findById(firstComment.getEvent().getId())
                .orElseThrow(() -> new EventNotExistException("Event not found"));
    }
}
