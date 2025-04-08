package ru.practicum;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.HitRequestDto;

import java.sql.Timestamp;
import java.time.Instant;

public class StatsClient extends BaseClient {

    private static final String APPLICATION = "ewm-main-service";

    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build()
        );
    }

    public void createHit(HttpServletRequest request) {
        final HitRequestDto hit = HitRequestDto.builder()
                .app(APPLICATION)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(Timestamp.from(Instant.now()))
                .build();
        post("/hit", hit);
    }
}
