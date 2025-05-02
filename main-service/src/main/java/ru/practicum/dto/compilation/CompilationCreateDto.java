package ru.practicum.dto.compilation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Size(min = 3, max = 50)
    private String title;
}
