package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HitResponseDto {
    private Long id;
    private String app;
    private Timestamp timestamp;
    private String uri;
    private String ip;
}
