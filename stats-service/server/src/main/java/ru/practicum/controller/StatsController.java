package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitRequestDto;
import ru.practicum.dto.HitResponseDto;
import ru.practicum.dto.ViewStatsResponseDto;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.model.HitBody;
import ru.practicum.model.ViewStats;
import ru.practicum.service.StatsService;

import java.sql.Timestamp;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
public class StatsController {
    private final StatsService statsService;
    private final StatsMapper statsMapper;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public HitResponseDto hit(@Valid @RequestBody HitRequestDto request) {
        log.info(request.toString());
        HitBody hitBody = statsService.save(statsMapper.toHit(request));
        return statsMapper.toHitResponseDto(hitBody);
    }

    @GetMapping("/stats")
    public List<ViewStatsResponseDto> stats(
            @RequestParam Timestamp start,
            @RequestParam Timestamp end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") boolean unique
    ) {
        log.info("start: {}, end: {}", start, end);
        List<ViewStats> viewStats = statsService.getViewStats(start, end, uris, unique);
        return statsMapper.toViewListResponse(viewStats);
    }
}
