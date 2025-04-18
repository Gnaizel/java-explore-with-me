package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.CreatedCategory;
import ru.practicum.model.Category;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public Category toCategory(CreatedCategory newCategoryDto) {
        if (newCategoryDto == null) {
            return null;
        }
        Category category = new Category();
        category.setName(newCategoryDto.getName());
        return category;
    }

    public CategoryDto toCategoryDto(Category category) {
        if (category == null) {
            return null;
        }
        return new CategoryDto(category.getId(), category.getName());
    }

    public List<CategoryDto> toCategoryDtoList(List<Category> categoryList) {
        if (categoryList == null) {
            return null;
        }
        return categoryList.stream()
                .map(this::toCategoryDto)
                .collect(Collectors.toList());
    }
}
