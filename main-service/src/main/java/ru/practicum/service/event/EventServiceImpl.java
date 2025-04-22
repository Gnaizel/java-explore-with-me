package ru.practicum.service.event;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.ViewStatsResponseDto;
import ru.practicum.dto.event.*;
import ru.practicum.enums.EventStat;
import ru.practicum.enums.SortValue;
import ru.practicum.enums.StatForUser;
import ru.practicum.exception.*;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.User;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.statistic.StatisticsService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepo;
    private final CategoryRepository categoryRepo;
    private final EventMapper eventMapper;

    private final UserRepository userRepo;

    private final EntityManager entityManager;
    private final StatisticsService statsService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public EventFullDto createEvent(Long userId, EventCreateDto newEvent) {
        validateEventDate(newEvent.getEventDate(), 2);
        Category category = categoryRepo.findById(newEvent.getCategory())
                .orElseThrow(() -> new CategoryNotExistException("Category not found"));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotExistException("User not found with id: " + userId));
        if (newEvent.getParticipantLimit() < 0) {
           throw  new EvetnValidationException("limit can't de negative");
        }
        if (newEvent.getAnnotation().isEmpty()) throw new RuntimeException("annotatnios can't de null");

        Event event = eventMapper.toEventModel(newEvent);
        event.setCategory(category);
        event.setInitiator(user);
        Event saveEvent = eventRepo.save(event);
        //  saveEvent.setViews(statsService.getStats(saveEvent.getCreatedOn(), LocalDateTime.now(), "/users/{userId}/events"));

        return eventMapper.toEventFullDto(saveEvent);
    }

    @Override
    public List<EventShortDto> getEvents(Long userId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return eventMapper.toEventShortDtoList(eventRepo.findAllByInitiatorId(userId, pageable).toList());
    }

    @Override
    public EventFullDto updateEvent(Long eventId, EventUpdateAdmDto adminUpdateDto) {
        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new EventNotExistException("Event not found with id: " + eventId));

        if (adminUpdateDto == null) return eventMapper.toEventFullDto(event);

        if (adminUpdateDto.getParticipantLimit() == null) {
            applyAdminUpdates(event, adminUpdateDto);
            return eventMapper.toEventFullDto(eventRepo.save(event));
        }

        if (adminUpdateDto.getParticipantLimit() < 0) {
            throw new EvetnValidationException("limit can't de negative");
        }


        return eventMapper.toEventFullDto(eventRepo.save(event));
    }

    @Override
    public EventFullDto updateEventByUser(Long userId, Long eventId, EventUpdateUserDto userUpdateDto) {
        Event event = eventRepo.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new EventNotExistException("Event not found"));

        if (event.getPublishedOn() != null) throw new AlreadyPublishedException("Event already published");
        if (userUpdateDto == null) return eventMapper.toEventFullDto(event);

        if (userUpdateDto.getParticipantLimit() < 0) {
            throw new EvetnValidationException("limit can't de negative");
        }

        applyUserUpdates(event, userUpdateDto);

        return eventMapper.toEventFullDto(eventRepo.save(event));
    }

    @Override
    public EventFullDto getEventByUser(Long userId, Long eventId) {
        return eventMapper.toEventFullDto(
                eventRepo.findByIdAndInitiatorId(eventId, userId)
                        .orElseThrow(() -> new EventNotExistException("Event not found")));
    }

    @Override
    public List<EventFullDto> getEventsWithParamsByAdmin(List<Long> users, EventStat states, List<Long> categoriesId,
                                                         String rangeStart, String rangeEnd, Integer from, Integer size) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> root = query.from(Event.class);

        List<Predicate> predicates = new ArrayList<>();
        if (users != null && !users.isEmpty()) predicates.add(root.get("initiator").get("id").in(users));
        if (categoriesId != null && !categoriesId.isEmpty()) predicates.add(root.get("category").get("id").in(categoriesId));
        if (states != null) predicates.add(root.get("state").in(states));

        if (rangeStart != null)
            predicates.add(cb.greaterThanOrEqualTo(root.get("eventDate"), LocalDateTime.parse(rangeStart, formatter)));
        if (rangeEnd != null)
            predicates.add(cb.lessThanOrEqualTo(root.get("eventDate"), LocalDateTime.parse(rangeEnd, formatter)));

        query.select(root).where(cb.and(predicates.toArray(new Predicate[0])));

        List<Event> events = entityManager.createQuery(query)
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();

        if (events.isEmpty()) return Collections.emptyList();

        assignViewCounts(events);
        return events.stream().map(eventMapper::toEventFullDto).toList();
    }

    @Override
    public List<EventFullDto> getEventsWithParamsByUser(String text, List<Long> categoryIds, Boolean paid,
                                                        String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                                        SortValue sort, Integer from, Integer size, HttpServletRequest request) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> root = query.from(Event.class);

        List<Predicate> filters = new ArrayList<>();
        if (text != null && !text.isBlank()) {
            String likeText = "%" + text.toLowerCase() + "%";
            filters.add(cb.or(
                    cb.like(cb.lower(root.get("annotation")), likeText),
                    cb.like(cb.lower(root.get("description")), likeText)));
        }

        if (categoryIds != null && !categoryIds.isEmpty()) filters.add(root.get("category").get("id").in(categoryIds));
        if (paid != null) filters.add(paid ? cb.isTrue(root.get("paid")) : cb.isFalse(root.get("paid")));

        if (rangeStart != null)
            filters.add(cb.greaterThanOrEqualTo(root.get("eventDate"), LocalDateTime.parse(rangeStart, formatter)));
        if (rangeEnd != null)
            filters.add(cb.lessThanOrEqualTo(root.get("eventDate"), LocalDateTime.parse(rangeEnd, formatter)));

        query.select(root).where(cb.and(filters.toArray(new Predicate[0]))).orderBy(cb.asc(root.get("eventDate")));

        List<Event> events = entityManager.createQuery(query)
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();

        if (onlyAvailable != null && onlyAvailable)
            events = events.stream().filter(e -> e.getConfirmedRequests() < e.getParticipantLimit()).collect(Collectors.toList());

        if (sort != null) {
            Comparator<Event> comparator = sort == SortValue.EVENT_DATE
                    ? Comparator.comparing(Event::getEventDate)
                    : Comparator.comparing(Event::getViews);
            events.sort(comparator);
        }

        if (events.isEmpty()) return Collections.emptyList();

        assignViewCounts(events);
        statsService.sendStat(events, request);

        return events.stream().map(eventMapper::toEventFullDto).toList();
    }

    @Override
    public EventFullDto getEvent(Long id, HttpServletRequest request) {
        Event event = eventRepo.findByIdAndPublishedOnIsNotNull(id)
                .orElseThrow(() -> new EventNotExistException("Published event not found with id: " + id));

        event = statsService.setView(event);
        statsService.sendStat(event, request);
        log.debug("event: {}", event.toString());
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public void setView(List<Event> events) {
        assignViewCounts(events);
    }

    private void validateEventDate(LocalDateTime eventDate, int hoursAhead) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(hoursAhead))) {
            throw new WrongTimeException("Event date must be at least " + hoursAhead + " hours in the future.");
        }
    }

    private void applyAdminUpdates(Event event, EventUpdateAdmDto dto) {
        Optional.ofNullable(dto.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(dto.getCategory()).ifPresent(catId ->
                event.setCategory(categoryRepo.findById(catId).orElseThrow(() -> new CategoryNotExistException("Category not found"))));
        Optional.ofNullable(dto.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(dto.getLocation()).ifPresent(event::setLocation);
        Optional.ofNullable(dto.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(dto.getParticipantLimit()).ifPresent(limit -> event.setParticipantLimit(limit.intValue()));
        Optional.ofNullable(dto.getRequestModeration()).ifPresent(event::setRequestModeration);
        Optional.ofNullable(dto.getTitle()).ifPresent(event::setTitle);

        if (dto.getStateAction() != null) {
            switch (dto.getStateAction()) {
                case PUBLISH_EVENT -> validateAndPublish(event);
                case REJECT_EVENT -> rejectEvent(event);
            }
        }

        if (dto.getEventDate() != null) {
            if (dto.getEventDate().isBefore(LocalDateTime.now()) ||
                    (event.getPublishedOn() != null && dto.getEventDate().isBefore(event.getPublishedOn().plusHours(1)))) {
                throw new WrongTimeException("Event date is invalid.");
            }
            event.setEventDate(dto.getEventDate());
        }
    }

    private void applyUserUpdates(Event event, EventUpdateUserDto dto) {
        Optional.ofNullable(dto.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(dto.getCategory()).ifPresent(catId ->
                event.setCategory(categoryRepo.findById(catId).orElseThrow(() -> new CategoryNotExistException("Category not found"))));
        Optional.ofNullable(dto.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(dto.getEventDate()).ifPresent(date -> {
            validateEventDate(date, 2);
            event.setEventDate(date);
        });
        Optional.ofNullable(dto.getLocation()).ifPresent(event::setLocation);
        Optional.ofNullable(dto.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(dto.getParticipantLimit()).ifPresent(limit -> event.setParticipantLimit(limit.intValue()));
        Optional.ofNullable(dto.getRequestModeration()).ifPresent(event::setRequestModeration);
        Optional.ofNullable(dto.getTitle()).ifPresent(event::setTitle);

        if (dto.getStateAction() != null) {
            if (dto.getStateAction() == StatForUser.SEND_TO_REVIEW) {
                event.setState(EventStat.PENDING);
            } else {
                event.setState(EventStat.CANCELED);
            }
        }
    }

    private void validateAndPublish(Event event) {
        if (event.getPublishedOn() != null) throw new AlreadyPublishedException("Event already published");
        if (event.getState() == EventStat.CANCELED) throw new EventAlreadyCanceledException("Event is canceled");
        event.setPublishedOn(LocalDateTime.now());
        event.setState(EventStat.PUBLISHED);
    }

    private void rejectEvent(Event event) {
        if (event.getPublishedOn() != null) throw new AlreadyPublishedException("Event already published");
        event.setState(EventStat.CANCELED);
    }

    private void assignViewCounts(List<Event> events) {
        if (events.isEmpty()) return;

        LocalDateTime latestStart = events.stream()
                .map(Event::getCreatedOn)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now());

        String startTime = latestStart.format(formatter);
        String endTime = LocalDateTime.now().format(formatter);

        Map<String, Event> uriToEventMap = new HashMap<>();
        List<String> uris = new ArrayList<>();

        for (Event event : events) {
            String uri = "/events/" + event.getId();
            uris.add(uri);
            uriToEventMap.put(uri, event);
            event.setViews(0L);
        }

        List<ViewStatsResponseDto> stats = statsService.getStats(startTime, endTime, uris);
        stats.forEach(stat -> {
            Event e = uriToEventMap.get(stat.getUri());
            if (e != null) e.setViews(stat.getHits());
        });
    }
}