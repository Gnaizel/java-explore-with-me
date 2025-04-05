package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.model.HitBody;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.StatsRepository;

import java.sql.Timestamp;
import java.util.List;

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
