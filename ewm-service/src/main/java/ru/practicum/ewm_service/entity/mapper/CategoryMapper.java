package ru.practicum.ewm_service.entity.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm_service.entity.Category;
import ru.practicum.ewm_service.entity.model.category.CategoryDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toCategory(CategoryDto categoryDto);

    CategoryDto toCategoryDto(Category category);

    List<CategoryDto> toCategoryDtos(List<Category> categories);
}
