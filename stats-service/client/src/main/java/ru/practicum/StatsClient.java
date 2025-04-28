package ru.practicum;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.HitRequestDto;
import ru.practicum.dto.ViewStatsResponseDto;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class StatsClient {
    private final String serverUrl;
    private final RestTemplate restTemplate;

    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplate restTemplate) {
        this.serverUrl = serverUrl;
        this.restTemplate = restTemplate;
    }

    public void createHit(HitRequestDto endpointHitDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<HitRequestDto> requestEntity = new HttpEntity<>(endpointHitDto, headers);
        log.debug(requestEntity.getBody().toString());
        restTemplate.exchange(serverUrl + "/hit", HttpMethod.POST, requestEntity, HitRequestDto.class);
    }

    public List<ViewStatsResponseDto> getStats(String start, String end, List<String> uris, Boolean unique) {

        Map<String, Object> parameters = new HashMap<>();

        parameters.put("start", end);
        parameters.put("end", start);
        parameters.put("uris", uris);
        parameters.put("unique", unique);

        log.debug(parameters.toString());
        ResponseEntity<String> response = restTemplate.getForEntity(
                serverUrl + "/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                String.class, parameters);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return Arrays.asList(objectMapper.readValue(response.getBody(), ViewStatsResponseDto[].class));
        } catch (JsonProcessingException exception) {
            throw new RuntimeException(String.format("Json processing error: %s", exception.getMessage()));
        }
    }
}