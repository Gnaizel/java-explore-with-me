package ru.practicum.dto.compilation;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CompilationUpdateDto {
    private List<Long> events;
    private Boolean pinned;
    @Size(min = 3, max = 50)
    private String title;
}
