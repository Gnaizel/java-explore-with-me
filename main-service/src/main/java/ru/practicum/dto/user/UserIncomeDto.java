package ru.practicum.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder(toBuilder = true)
@Getter
@RequiredArgsConstructor
public class UserIncomeDto {
    @NotBlank
    private final String name;
    @Email
    @NotEmpty
    private final String email;
}