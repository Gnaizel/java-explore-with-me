package ru.practicum.controller.authorized;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentCreateDto;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.event.EventCreateDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.EventUpdateUserDto;
import ru.practicum.dto.request.RequestDto;
import ru.practicum.dto.request.RequestStatusUpdateDto;
import ru.practicum.dto.request.RequestStatusUpdateResult;
import ru.practicum.service.comment.CommentService;
import ru.practicum.service.event.EventService;
import ru.practicum.service.request.RequestService;

import java.util.List;


@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AuthEventController {
    private final EventService eventService;

    private final RequestService requestService;
    private final CommentService commentService;

    @PostMapping("/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@PathVariable Long eventId,
                                    @PathVariable Long userId,
                                    @Valid @RequestBody CommentCreateDto commentCreateDto) {
        log.debug("comment create endpoint: \n userid: {} \n eventID: {} \n comment: {}", userId, eventId, commentCreateDto.toString());

        return commentService.createComment(eventId, userId, commentCreateDto);
    }

    @PatchMapping("/comments/{commentId}")
    public CommentDto updateComment(@PathVariable Long userId,
                                    @PathVariable Long commentId,
                                    @Valid @RequestBody CommentCreateDto commentCreateDto) {
        log.debug("comment update endpoint: \n userid: {} \n \n comment: {}", userId, commentCreateDto.toString());

        return commentService.updateComment(userId, commentId, commentCreateDto);
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long userId,
                              @PathVariable Long commentId) {
        log.debug("comment delete endpoint: \n userid: {}", userId);

        commentService.deleteComment(userId, commentId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable Long userId, @Valid @RequestBody EventCreateDto eventCreateDto) {
        log.debug("request: {}", eventCreateDto.toString());
        return eventService.createEvent(userId, eventCreateDto);
    }

    @GetMapping
    public List<EventShortDto> getEventsByUser(@PathVariable Long userId,
                                               @RequestParam(name = "from", defaultValue = "0", required = false) Integer from,
                                               @RequestParam(name = "size", defaultValue = "10", required = false) Integer size) {
        return eventService.getEvents(userId, from, size);
    }

    @GetMapping("/{eventId}/requests")
    public List<RequestDto> getRequestsByOwnerOfEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return requestService.getRequestsByOwnerOfEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public RequestStatusUpdateResult updateRequests(@PathVariable Long userId, @PathVariable Long eventId,
                                                    @RequestBody(required = false) RequestStatusUpdateDto requestStatusUpdateDto) {
        return requestService.updateRequests(userId, eventId, requestStatusUpdateDto);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByUser(@PathVariable Long userId,
                                          @PathVariable Long eventId,
                                          @Valid @RequestBody EventUpdateUserDto eventUpdateUserDto) {
        return eventService.updateEventByUser(userId, eventId, eventUpdateUserDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventByUser(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.getEventByUser(userId, eventId);
    }
}