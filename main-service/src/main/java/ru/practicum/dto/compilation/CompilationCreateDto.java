package ru.practicum.dto.compilation;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilationCreateDto {
    private List<Long> events;
    private Boolean pinned;
    @NotBlank
    private String title;
}
