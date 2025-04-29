package ru.practicum.service.statistic;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.StatsClient;
import ru.practicum.dto.HitRequestDto;
import ru.practicum.dto.ViewStatsResponseDto;
import ru.practicum.model.Event;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final StatsClient statsClient;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final String nameService = "main-service";

    @Override
    public void sendStat(Event event, HttpServletRequest request) {
        Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now());
        String remoteAddr = request.getRemoteAddr();

        HitRequestDto requestDto = HitRequestDto.builder()
                .timestamp(currentTime)
                .app(nameService)
                .uri("/events")
                .ip(remoteAddr)
                .build();

        statsClient.createHit(requestDto);
        sendStatForTheEvent(event.getId(), remoteAddr, currentTime, "main-service");
    }

    @Override
    public void sendStat(List<Event> events, HttpServletRequest request) {
        Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now());
        String remoteAddr = request.getRemoteAddr();

        HitRequestDto requestDto = HitRequestDto.builder()
                .timestamp(currentTime)
                .app(nameService)
                .uri("/events")
                .ip(remoteAddr)
                .build();

        statsClient.createHit(requestDto);
        sendStatForEveryEvent(events, remoteAddr, currentTime, "main-service");
    }

    @Override
    public void sendStatForTheEvent(Long eventId, String remoteAddr, Timestamp now,
                                    String nameService) {
        HitRequestDto requestDto = HitRequestDto.builder()
                .timestamp(now)
                .uri("/events/" + eventId)
                .app(nameService)
                .ip(remoteAddr)
                .build();
        log.info("app: {} \nuri: {}\nip: {}\ntimestemp:{}\n",
                requestDto.getApp(),
                requestDto.getUri(),
                requestDto.getIp(),
                requestDto.getTimestamp());

        statsClient.createHit(requestDto);
    }

    @Override
    public void sendStatForEveryEvent(List<Event> events, String remoteAddr, Timestamp now,
                                      String nameService) {
        events.forEach(event -> {
            HitRequestDto requestDto = HitRequestDto.builder()
                    .timestamp(now)
                    .uri("/events/" + event.getId())
                    .ip(remoteAddr)
                    .app(nameService)
                    .build();
            log.info("app: {} \nuri: {}\nip: {}\ntimestemp:{}\n",
                    requestDto.getApp(),
                    requestDto.getUri(),
                    requestDto.getIp(),
                    requestDto.getTimestamp());

            statsClient.createHit(requestDto);
        });
    }

    @Override
    public Event setView(Event event) {
        String startTime = event.getCreatedOn().minusMinutes(1).format(formatter);
        String endTime = LocalDateTime.now().plusMinutes(1).format(formatter);
        List<String> uris = List.of("/events/" + event.getId());

        List<ViewStatsResponseDto> stats = getStats(startTime, endTime, uris);
        event.setViews(stats.isEmpty() ? 0L : stats.get(0).getHits());
        return event;
    }

    @Override
    public List<ViewStatsResponseDto> getStats(String startTime, String endTime, List<String> uris) {
        return statsClient.getStats(startTime, endTime, uris, true);
    }
}
