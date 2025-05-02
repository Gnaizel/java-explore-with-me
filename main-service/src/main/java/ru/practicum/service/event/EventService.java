package ru.practicum.service.event;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.dto.event.*;
import ru.practicum.enums.EventStat;
import ru.practicum.enums.SortValue;
import ru.practicum.model.Event;

import java.util.List;

public interface EventService {

    EventFullDto createEvent(Long userId, EventCreateDto newEvent);

    List<EventShortDto> getEvents(Long userId, Integer from, Integer size);

    EventFullDto updateEvent(Long eventId, EventUpdateAdmDto updateAdmDto);

    EventFullDto updateEventByUser(Long userId, Long eventId, EventUpdateUserDto updateUserEvent);

    EventFullDto getEventByUser(Long userId, Long eventId);

    List<EventFullDto> getEventsWithParamsByAdmin(List<Long> users, List<EventStat> states, List<Long> categoriesId,
                                                  String rangeStart, String rangeEnd, Integer from, Integer size);

    List<EventFullDto> getEventsWithParamsByUser(String text, List<Long> categories, Boolean paid, String rangeStart,
                                                 String rangeEnd, Boolean onlyAvailable, SortValue sort, Integer from,
                                                 Integer size, HttpServletRequest request);

    EventFullDto getEvent(Long id, HttpServletRequest request);

    void setView(List<Event> events);
}
