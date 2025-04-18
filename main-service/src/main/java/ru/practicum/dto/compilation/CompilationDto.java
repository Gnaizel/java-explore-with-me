package ru.practicum.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.event.EventShortDto;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CompilationDto {
    private long id;
    private List<EventShortDto> events;
    private boolean pined;
    private String title;
}
