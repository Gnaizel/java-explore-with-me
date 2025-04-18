package ru.practicum.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class RequestStatusUpdateResult {
    private List<RequestDto> confirmedRequests;
    private List<RequestDto> rejectedRequests;
}