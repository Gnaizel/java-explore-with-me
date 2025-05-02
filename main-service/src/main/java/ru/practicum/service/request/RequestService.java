package ru.practicum.service.request;

import ru.practicum.dto.request.RequestDto;
import ru.practicum.dto.request.RequestStatusUpdateDto;
import ru.practicum.dto.request.RequestStatusUpdateResult;

import java.util.List;

public interface RequestService {

    RequestDto createRequest(Long userId, Long eventId);

    List<RequestDto> getRequestsByOwnerOfEvent(Long userId, Long eventId);

    RequestStatusUpdateResult updateRequests(Long userId, Long eventId, RequestStatusUpdateDto requestStatusUpdateDto);

    List<RequestDto> getCurrentUserRequests(Long userId);

    RequestDto cancelRequests(Long userId, Long requestId);
}