package ru.practicum.mapper;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.model.Category;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Service
@AllArgsConstructor
public class CompilationMapper {

    private final EventShortMapper eventShortMapper;

    public CompilationDto mapToCompilationDto(Compilation compilation) {
        if (compilation == null) {
            return null;
        }

        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());

        if (compilation.getEvents() != null) {
            compilationDto.setEvents(compilation.getEvents().stream()
                    .map(eventShortMapper::mapToEventShortDto)
                    .collect(Collectors.toList()));
        } else {
            compilationDto.setEvents(null);
        }

        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setTitle(compilation.getTitle());

        return compilationDto;
    }

    public List<CompilationDto> mapToListCompilationDto(List<Compilation> compilations) {
        if (compilations == null) {
            return null;
        }

        return compilations.stream()
                .map(this::mapToCompilationDto)
                .collect(Collectors.toList());
    }

    @Component
    @RequiredArgsConstructor
    static class EventShortMapper {

        private final CategoryMapper categoryMapper;
        private final UserShortMapper userShortMapper;

        public EventShortDto mapToEventShortDto(Event event) {
            if (event == null) {
                return null;
            }

            EventShortDto eventShortDto = new EventShortDto();
            eventShortDto.setId(event.getId());
            eventShortDto.setAnnotation(event.getAnnotation());
            eventShortDto.setCategory(categoryMapper.mapToCategoryDto(event.getCategory())); // Преобразуем Category в CategoryDto
            eventShortDto.setConfirmedRequests(event.getConfirmedRequests());
            eventShortDto.setEventDate(event.getEventDate());
            eventShortDto.setInitiator(userShortMapper.mapToUserShortDto(event.getInitiator())); // Преобразуем User в UserShortDto
            eventShortDto.setPaid(event.getPaid());
            eventShortDto.setTitle(event.getTitle());
            eventShortDto.setViews(event.getViews());

            return eventShortDto;
        }

        @Component
        static class CategoryMapper {
            public CategoryDto mapToCategoryDto(Category category) {
                if (category == null) {
                    return null;
                }
                CategoryDto categoryDto = new CategoryDto();
                categoryDto.setId(category.getId());
                categoryDto.setName(category.getName());
                return categoryDto;
            }
        }

        @Component
        static class UserShortMapper {
            public UserShortDto mapToUserShortDto(User user) {
                if (user == null) {
                    return null;
                }
                UserShortDto userShortDto = new UserShortDto();
                userShortDto.setId(user.getId());
                userShortDto.setName(user.getName());
                return userShortDto;
            }
        }
    }
}