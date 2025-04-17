package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.event.EventCreateDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.model.Category;
import ru.practicum.model.Event;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;

    public EventMapper(CategoryMapper categoryMapper,
                       UserMapper userMapper) {
        this.categoryMapper = categoryMapper;
        this.userMapper = userMapper;
    }

    public EventFullDto toEventFullDto(Event event) {
        if (event == null) {
            return null;
        }
        EventFullDto dto = new EventFullDto();
        dto.setAnnotation(event.getAnnotation());
        dto.setCategory(categoryMapper.toCategoryDto(event.getCategory()));
        dto.setConfirmedRequests(event.getConfirmedRequests());
        if (event.getCreatedOn() != null) {
            dto.setCreatedOn(event.getCreatedOn().format(FORMATTER));
        }
        dto.setDescription(event.getDescription());
        dto.setEventDate(event.getEventDate());
        dto.setId(event.getId());
        dto.setInitiator(userMapper.toShortDto(event.getInitiator()));
        dto.setLocation(event.getLocation());
        dto.setPaid(event.getPaid());
        dto.setParticipantLimit((long) event.getParticipantLimit());
        dto.setPublishedOn(event.getPublishedOn());
        dto.setRequestModeration(event.getRequestModeration());
        dto.setState(event.getState());
        dto.setTitle(event.getTitle());
        dto.setViews(event.getViews());
        return dto;
    }

    public Event toEventModel(EventCreateDto dto) {
        if (dto == null) {
            return null;
        }
        Event event = new Event();
        event.setAnnotation(dto.getAnnotation());
        event.setCategory(new Category(dto.getCategory(), null));
        event.setDescription(dto.getDescription());
        event.setEventDate(dto.getEventDate());
        event.setLocation(dto.getLocation());
        event.setPaid(dto.isPaid());
        event.setParticipantLimit(dto.getParticipantLimit());
        event.setRequestModeration(dto.getRequestModeration());
        event.setTitle(dto.getTitle());
        return event;
    }

    public EventShortDto toEventShortDto(Event event) {
        if (event == null) {
            return null;
        }
        EventShortDto dto = new EventShortDto();
        dto.setId(event.getId());
        dto.setAnnotation(event.getAnnotation());
        dto.setCategory(categoryMapper.toCategoryDto(event.getCategory()));
        dto.setConfirmedRequests(event.getConfirmedRequests());
        dto.setEventDate(event.getEventDate());
        dto.setInitiator(userMapper.toShortDto(event.getInitiator()));
        dto.setPaid(event.getPaid());
        dto.setTitle(event.getTitle());
        dto.setViews(event.getViews());
        return dto;
    }

    public List<EventShortDto> toEventShortDtoList(List<Event> events) {
        if (events == null) {
            return null;
        }
        return events.stream()
                .map(this::toEventShortDto)
                .collect(Collectors.toList());
    }

    public List<EventFullDto> toEventFullDtoList(List<Event> events) {
        if (events == null) {
            return null;
        }
        return events.stream()
                .map(this::toEventFullDto)
                .collect(Collectors.toList());
    }
}
