package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.request.RequestDto;
import ru.practicum.model.Request;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    RequestDto toRequestDto(Request request);

    List<RequestDto> toRequestDtoList(List<Request> requests);
}