package ru.practicum.controller.authorized;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.RequestDto;
import ru.practicum.service.request.RequestService;

import java.util.List;


@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class AuthRequestController {
    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto createRequest(@PathVariable(name = "userId") Long userId, @RequestParam(name = "eventId") Long eventId) {
        return requestService.createRequest(userId, eventId);
    }

    @GetMapping
    public List<RequestDto> getCurrentUserRequests(@PathVariable(name = "userId") Long userId) {
        return requestService.getCurrentUserRequests(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable(name = "userId") Long userId, @PathVariable Long requestId) {
        return requestService.cancelRequests(userId, requestId);
    }
}