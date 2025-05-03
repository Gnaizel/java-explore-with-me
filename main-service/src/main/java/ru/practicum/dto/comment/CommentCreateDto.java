package ru.practicum.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CommentCreateDto {
    @NotBlank
    @Size(min = 20, max = 2000)
    private String comment;
}
