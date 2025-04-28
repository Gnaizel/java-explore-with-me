package ru.practicum.service.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.request.RequestDto;
import ru.practicum.dto.request.RequestStatusUpdateDto;
import ru.practicum.dto.request.RequestStatusUpdateResult;
import ru.practicum.enums.RequestStatus;
import ru.practicum.enums.RequestStatusToUpdate;
import ru.practicum.exception.*;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public List<RequestDto> getRequestsByOwnerOfEvent(Long userId, Long eventId) {
        return RequestMapper.toRequestDtoList(requestRepository.findAllByEventWithInitiator(userId, eventId));
    }

    @Override
    public RequestDto createRequest(Long userId, Long eventId) {
        if (requestRepository.existsByRequesterAndEvent(userId, eventId)) {
            throw new RequestAlreadyExistException("Request already exists");
        }
        log.debug(eventId.toString());
//        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotExistException("Event doesnt exist"));
        Optional<Event> event1 = eventRepository.findById(eventId);
        if (!event1.isPresent()) {
            throw new EvetnValidationException("Event doesnt exist");
        }
        Event event = event1.get();
        if (event.getInitiator().getId().equals(userId)) {
            throw new WrongUserException("Can't create request by initiator");
        }

        if (event.getPublishedOn() == null) {
            throw new EventIsNotPublishedException("Event is not published yet");
        }

        List<Request> requests = requestRepository.findAllByEventAndStatus(eventId, RequestStatus.CONFIRMED);

        if (requests.size() >= event.getParticipantLimit() && event.getParticipantLimit() != 0) {
            log.debug("\nrequests : {}\n limit: {}", requests.size(), event.getParticipantLimit());
            throw new ParticipantLimitException("Member limit exceeded ");
        }
        log.debug("\nrequests : {}\n limit: {}", requests.size(), event.getParticipantLimit());

        Request request = new Request();
        request.setCreated(LocalDateTime.now());
        request.setEvent(eventId);
        request.setRequester(userId);
        request.setStatus(event.getRequestModeration() ? RequestStatus.PENDING : RequestStatus.CONFIRMED);
        if (event.getParticipantLimit() == 0) request.setStatus(RequestStatus.CONFIRMED);
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

//    @Transactional
//    @Override
//    public RequestStatusUpdateResult updateRequests(Long userId, Long eventId, RequestStatusUpdateDto requestStatusUpdateDto) {
//        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotExistException("Event doesn't exist"));
//        RequestStatusUpdateResult result = new RequestStatusUpdateResult();
//
//        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
//            return result;
//        }
//
//        List<Request> requests = requestRepository.findAllByEventWithInitiator(userId, eventId);
//        List<Request> requestsToUpdate = requests.stream().filter(x -> requestStatusUpdateDto.getRequestIds().contains(x.getId())).collect(Collectors.toList());
//
//        if (requestsToUpdate.stream().anyMatch(x -> x.getStatus().equals(RequestStatus.CONFIRMED) && requestStatusUpdateDto.getStatus().equals(RequestStatusToUpdate.REJECTED))) {
//            throw new RequestAlreadyConfirmedException("request already confirmed");
//        }
//
//        if (event.getConfirmedRequests() + requestsToUpdate.size() > event.getParticipantLimit() && requestStatusUpdateDto.getStatus().equals(RequestStatusToUpdate.CONFIRMED)) {
//            throw new ParticipantLimitException("exceeding the limit of participants");
//        }
//
//        for (Request x : requestsToUpdate) {
//            x.setStatus(RequestStatus.valueOf(requestStatusUpdateDto.getStatus().toString()));
//        }
//
//        requestRepository.saveAll(requestsToUpdate);
//
//        if (requestStatusUpdateDto.getStatus().equals(RequestStatusToUpdate.CONFIRMED)) {
//            event.setConfirmedRequests(event.getConfirmedRequests() + requestsToUpdate.size());
//        }
//
//        eventRepository.save(event);
//
//        if (requestStatusUpdateDto.getStatus().equals(RequestStatusToUpdate.CONFIRMED)) {
//            result.setConfirmedRequests(requestMapper.toRequestDtoList(requestsToUpdate));
//        }
//
//        if (requestStatusUpdateDto.getStatus().equals(RequestStatusToUpdate.REJECTED)) {
//            result.setRejectedRequests(requestMapper.toRequestDtoList(requestsToUpdate));
//        }
//
//        return result;
//    }

    @Transactional
    @Override
    public RequestStatusUpdateResult updateRequests(Long userId, Long eventId, RequestStatusUpdateDto requestStatusUpdateDto) {
        if (requestStatusUpdateDto != null) {
            if (requestStatusUpdateDto.getRequestIds() == null
                    || requestStatusUpdateDto.getRequestIds().stream().anyMatch(Objects::isNull)) {
                throw new InvalidRequestException("Request IDs cannot be null");
            }
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotExistException("Event doesn't exist"));

        RequestStatusUpdateResult result = new RequestStatusUpdateResult();

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            return result;
        }

        List<Request> requests = requestRepository.findAllByEventWithInitiator(userId, eventId);

        List<Request> requestsToUpdate = requests.stream()
                .filter(x -> requestStatusUpdateDto.getRequestIds().contains(x.getId()))
                .collect(Collectors.toList());

        if (requestsToUpdate.isEmpty()) {
            throw new InvalidRequestException("No valid requests found");
        }

        if (requestStatusUpdateDto.getStatus() == RequestStatusToUpdate.REJECTED) {
            requestsToUpdate.forEach(request -> {
                if (request.getStatus() == RequestStatus.CONFIRMED) {
                    throw new RequestAlreadyConfirmedException(
                            "Cannot reject already confirmed request ID: " + request.getId());
                }
            });
        }

        if (requestStatusUpdateDto.getStatus() == RequestStatusToUpdate.CONFIRMED) {
            long availableSlots = event.getParticipantLimit() - event.getConfirmedRequests();
            if (availableSlots < requestsToUpdate.size()) {
                throw new ParticipantLimitException(
                        "Only " + availableSlots + " slots available");
            }
            log.debug("Request confirmed | count: {}", event.getConfirmedRequests());
        }

        requestsToUpdate.forEach(request ->
                request.setStatus(RequestStatus.valueOf(requestStatusUpdateDto.getStatus().name())));

        requestRepository.saveAll(requestsToUpdate);

        if (requestStatusUpdateDto.getStatus() == RequestStatusToUpdate.CONFIRMED) {
            event.setConfirmedRequests(event.getConfirmedRequests() + requestsToUpdate.size());
            eventRepository.save(event);
            result.setConfirmedRequests(RequestMapper.toRequestDtoList(requestsToUpdate));
        } else {
            result.setRejectedRequests(RequestMapper.toRequestDtoList(requestsToUpdate));
        }

        return result;
    }

    @Override
    public List<RequestDto> getCurrentUserRequests(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotExistException(String.format("User with id=%s was not found", userId)));
        return RequestMapper.toRequestDtoList(requestRepository.findAllByRequester(userId));
    }

    @Override
    public RequestDto cancelRequests(Long userId, Long requestId) {
        Request request = requestRepository.findByRequesterAndId(userId, requestId).orElseThrow(() -> new RequestNotExistException(String.format("Request with id=%s was not found", requestId)));
        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }
}