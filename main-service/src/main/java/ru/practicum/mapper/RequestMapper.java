package ru.practicum.mapper;

import ru.practicum.dto.request.RequestDto;
import ru.practicum.model.Request;

import java.util.ArrayList;
import java.util.List;

public class RequestMapper {

    public static RequestDto toRequestDto(Request request) {
        if (request == null) {
            return null;
        }

        RequestDto requestDto = new RequestDto();
        requestDto.setId(request.getId());
        requestDto.setCreated(request.getCreated());
        requestDto.setEvent(request.getEvent());
        requestDto.setRequester(request.getRequester());
        requestDto.setStatus(request.getStatus().toString()); // Преобразуем enum в String

        return requestDto;
    }

    public static List<RequestDto> toRequestDtoList(List<Request> requests) {
        if (requests == null) {
            return null;
        }

        List<RequestDto> requestDtoList = new ArrayList<>();
        for (Request request : requests) {
            requestDtoList.add(toRequestDto(request));
        }

        return requestDtoList;
    }
}
