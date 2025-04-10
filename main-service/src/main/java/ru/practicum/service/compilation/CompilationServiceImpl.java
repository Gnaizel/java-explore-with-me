package ru.practicum.service.compilation;

import org.springframework.stereotype.Service;
import ru.practicum.dto.compilation.CompilationCreateDto;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.CompilationUpdateDto;

import java.util.List;

@Service
public class CompilationServiceImpl implements CompilationService {
    @Override
    public CompilationDto createCompilation(CompilationCreateDto newCompilation) {
        return null;
    }

    @Override
    public CompilationDto getCompilation(long id) {
        return null;
    }

    @Override
    public List<CompilationDto> getCompilations(boolean pined, int from, int size) {
        return List.of();
    }

    @Override
    public CompilationDto updateCompilation(Long compId, CompilationUpdateDto compilationUpdate) {
        return null;
    }

    @Override
    public void deleteCompilation(Long compId) {

    }
}
