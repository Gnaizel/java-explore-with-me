package ru.practicum.dto;


import lombok.*;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EndpointHitRequest {
    private Long id;
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}