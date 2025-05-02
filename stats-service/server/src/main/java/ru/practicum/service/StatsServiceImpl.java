package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.exception.TimeValidationError;
import ru.practicum.model.HitBody;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.StatsRepository;

import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository repo;

    @Override
    public HitBody save(HitBody hitBody) {
        return repo.save(hitBody);
    }

    @Override
    public List<ViewStats> getViewStats(Timestamp start, Timestamp end, List<String> uris, boolean unique) {
        if (start.after(end)) {
            throw new TimeValidationError("End timestamp cannot be before start timestamp.");
        }

        log.debug("start: {} end: {} uris: {}", start, end, uris);
        if (uris == null || uris.isEmpty()) {
            if (unique) {
                return repo.findStatsByDatesUniqueIpAllUris(start, end);
            } else {
                return repo.findStatsByDatesAllUris(start, end);
            }
        } else {
            if (unique) {
                return repo.findStatsByDatesUniqueIp(start, end, uris);
            } else {
                return repo.findStatsByDates(start, end, uris);
            }
        }
    }
}
