package ru.practicum.controller.pub;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.enums.SortValue;
import ru.practicum.service.event.EventService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class PublicEventsController {

    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getEventsWithParamsByUser(@RequestParam(name = "text", required = false) String text,
                                                        @RequestParam(name = "categories", required = false) List<Long> categories,
                                                        @RequestParam(name = "paid", required = false) Boolean paid,
                                                        @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                                        @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                                        @RequestParam(name = "onlyAvailable", required = false) boolean onlyAvailable,
                                                        @RequestParam(name = "sort", required = false) SortValue sort,
                                                        @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
                                                        @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
                                                        HttpServletRequest request) {
        return eventService.getEventsWithParamsByUser(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/{id}")
    public EventFullDto getEvent(@PathVariable long id, HttpServletRequest request) {
        return eventService.getEvent(id, request);
    }
}
