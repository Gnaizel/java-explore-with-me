package ru.practicum.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.CreatedCategory;
import ru.practicum.exception.CategoryIsNotEmptyException;
import ru.practicum.exception.CategoryNotExistException;
import ru.practicum.exception.NameAlreadyExistException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto createCategory(CreatedCategory newCategoryDto) {
        if (categoryRepository.findByName(newCategoryDto.getName()).isPresent()) {
            throw new NameAlreadyExistException(String.format("Can't create category because name: %s already used by another category", newCategoryDto.getName()));
        }
        return categoryMapper.toCategoryDto(categoryRepository.save(categoryMapper.toCategory(newCategoryDto)));
    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        Pageable page = PageRequest.of(from / size, size);
        return categoryMapper.toCategoryDtoList(categoryRepository.findAll(page).toList());
    }

    @Override
    public CategoryDto getCategory(Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new CategoryNotExistException("Category doesn't exist"));
        return categoryMapper.toCategoryDto(category);
    }

    @Override
    public void deleteCategory(Long catId) {
        if (eventRepository.existsByCategoryId(catId)) {
            throw new CategoryIsNotEmptyException("The category is not empty");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new CategoryNotExistException("Category doesn't exist"));
        if (categoryDto.getName() == null || categoryDto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }

        Optional<Category> categoryOptional = categoryRepository.findByName(categoryDto.getName());

        if (categoryOptional.isPresent() && !categoryOptional.get().getId().equals(catId)) {
            throw new NameAlreadyExistException(String.format("Can't update category because name: %s already used by another category", categoryDto.getName()));
        }

        category.setName(categoryDto.getName());

        return categoryMapper.toCategoryDto(categoryRepository.save(category));
    }
}