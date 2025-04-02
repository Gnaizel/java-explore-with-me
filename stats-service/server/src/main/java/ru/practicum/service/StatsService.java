package ru.practicum.service;

import ru.practicum.model.HitBody;
import ru.practicum.model.ViewStats;

import java.sql.Timestamp;
import java.util.List;

public interface StatsService {

    HitBody save(HitBody hitBody);

    List<ViewStats> getViewStats(Timestamp start, Timestamp end, List<String> uris, boolean unique);
}
