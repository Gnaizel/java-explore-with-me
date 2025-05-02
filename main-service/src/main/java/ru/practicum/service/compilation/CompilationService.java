package ru.practicum.service.compilation;

import ru.practicum.dto.compilation.CompilationCreateDto;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.CompilationUpdateDto;

import java.util.List;

public interface CompilationService {

    CompilationDto createCompilation(CompilationCreateDto newCompilation);

    CompilationDto getCompilation(long id);

    List<CompilationDto> getCompilations(Boolean pined, int from, int size);

    CompilationDto updateCompilation(Long compId, CompilationUpdateDto compilationUpdate);

    void deleteCompilation(Long compId);
}
