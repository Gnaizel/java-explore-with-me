package ru.practicum.dto.compilation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @NotBlank
    @Size(min = 3, max = 50)
    private String title;
    private boolean pinned;
    private List<EventShortDto> events;
}
