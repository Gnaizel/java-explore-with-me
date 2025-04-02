package ru.practicum.mapper;

import org.springframework.stereotype.Service;
import ru.practicum.dto.HitRequestDto;
import ru.practicum.dto.HitResponseDto;
import ru.practicum.dto.ViewStatsResponseDto;
import ru.practicum.model.HitBody;
import ru.practicum.model.ViewStats;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatsMapper {
    public HitBody toHit(HitRequestDto request) {
        HitBody hitBody = HitBody.builder()
                .app(request.getApp())
                .ip(request.getIp())
                .timestamp(request.getTimestamp())
                .uri(request.getUri())
                .build();
        return hitBody;
    }

    public HitResponseDto toHitResponseDto(HitBody hitBody) {
        HitResponseDto hitResponseDto = HitResponseDto.builder()
                .id(hitBody.getId())
                .app(hitBody.getApp())
                .ip(hitBody.getIp())
                .timestamp(hitBody.getTimestamp())
                .uri(hitBody.getUri())
                .build();
        return hitResponseDto;
    }

    public ViewStatsResponseDto toViewStatsResponse(ViewStats viewStats) {
        ViewStatsResponseDto viewStatsResponseDto = ViewStatsResponseDto.builder()
                .app(viewStats.getApp())
                .uri(viewStats.getUri())
                .hits(viewStats.getHits())
                .build();
        return viewStatsResponseDto;
    }

    public List<ViewStatsResponseDto> toViewListResponse(List<ViewStats> viewStatsList) {
        return viewStatsList.stream()
                .map(viewStats -> toViewStatsResponse(viewStats))
                .collect(Collectors.toList());
    }
}
