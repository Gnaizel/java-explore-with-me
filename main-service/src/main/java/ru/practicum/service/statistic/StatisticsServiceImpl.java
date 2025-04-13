package ru.practicum.service.statistic;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final StatsClient statsClient;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final LocalDateTime now = LocalDateTime.now();

    @Override
    public void sendStat(Event event, HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        String nameService = "main-service";
        HitRequestDto requestDto = new HitRequestDto();
        requestDto.setTimestamp(Timestamp.valueOf(now.format(formatter)));
        requestDto.setUri("/events");
//        requestDto.setApp(nameService);
        requestDto.setIp(remoteAddr);
        statsClient.createHit(requestDto);
        sendStatForTheEvent(event.getId(), remoteAddr, now, nameService);
    }

    @Override
    public void sendStat(List<Event> events, HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        String nameService = "main-service";
        HitRequestDto requestDto = new HitRequestDto();
        requestDto.setTimestamp(Timestamp.valueOf(now.format(formatter)));
        requestDto.setUri("/events");
//        requestDto.setApp(nameService);
        requestDto.setIp(request.getRemoteAddr());
        statsClient.createHit(requestDto);
        sendStatForEveryEvent(events, remoteAddr, LocalDateTime.now(), nameService);
    }

    @Override
    public void sendStatForTheEvent(Long eventId, String remoteAddr, LocalDateTime now,
                                    String nameService) {
        HitRequestDto requestDto = new HitRequestDto();
        requestDto.setTimestamp(Timestamp.valueOf(now.format(formatter)));
        requestDto.setUri("/events/" + eventId);
//        requestDto.setApp(nameService);
        requestDto.setIp(remoteAddr);
        statsClient.createHit(requestDto);
    }

    @Override
    public void sendStatForEveryEvent(List<Event> events, String remoteAddr, LocalDateTime now,
                                      String nameService) {
        for (Event event : events) {
            HitRequestDto requestDto = new HitRequestDto();
            requestDto.setTimestamp(Timestamp.valueOf(now.format(formatter)));
            requestDto.setUri("/events/" + event.getId());
//            requestDto.setApp(nameService);
            requestDto.setIp(remoteAddr);
            statsClient.createHit(requestDto);
        }
    }

    @Override
    public void setView(Event event) {
        String startTime = event.getCreatedOn().format(formatter);
        String endTime = LocalDateTime.now().format(formatter);
        List<String> uris = List.of("/events/" + event.getId());

        List<ViewStatsResponseDto> stats = getStats(startTime, endTime, uris);
        if (stats.size() == 1) {
            event.setViews(stats.get(0).getHits());
        } else {
            event.setViews(0L);
        }
    }

    @Override
    public List<ViewStatsResponseDto> getStats(String startTime, String endTime, List<String> uris) {
        return statsClient.getStats(startTime, endTime, uris, false);
    }
}
