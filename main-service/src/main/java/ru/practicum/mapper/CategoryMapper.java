package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.CreatedCategory;
import ru.practicum.model.Category;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface CategoryMapper {
    Category toCategory(CreatedCategory newCategoryDto);

    CategoryDto toCategoryDto(Category category);

    List<CategoryDto> toCategoryDtoList(List<Category> categoryList);
}