package ru.practicum.service.statistic;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.dto.ViewStatsResponseDto;
import ru.practicum.model.Event;

import java.sql.Timestamp;
import java.util.List;

public interface StatisticsService {
    void sendStat(Event event, HttpServletRequest request);

    void sendStat(List<Event> events, HttpServletRequest request);

    void sendStatForTheEvent(Long eventId, String remoteAddr, Timestamp now,
                             String nameService);

    void sendStatForEveryEvent(List<Event> events, String remoteAddr, Timestamp now,
                               String nameService);

    Event setView(Event event);

    List<ViewStatsResponseDto> getStats(String startTime, String endTime, List<String> uris);
}
