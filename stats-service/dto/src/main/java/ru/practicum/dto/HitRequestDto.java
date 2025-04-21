package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HitRequestDto {
    private Long id;
    @NotBlank
    private String app;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX") // формат с часовым поясом
    private OffsetDateTime timestamp;
    @NotBlank
    private String uri;
    @NotBlank
    private String ip;
}
