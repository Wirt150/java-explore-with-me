package ru.practicum.ewm_service.web.publics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_service.entity.Category;
import ru.practicum.ewm_service.entity.mapper.CategoryMapper;
import ru.practicum.ewm_service.entity.model.category.CategoryDto;
import ru.practicum.ewm_service.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class PublicCategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @Autowired
    public PublicCategoryController(CategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping("/{compId}")
    public ResponseEntity<CategoryDto> getCategory(
            @PathVariable(value = "compId") final long compId
    ) {
        Category category = categoryService.getCategoryById(compId);
        return new ResponseEntity<>(categoryMapper.toCategoryDto(category), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories(
            @RequestParam(defaultValue = "0") final int from,
            @RequestParam(defaultValue = "10") final int size
    ) {
        List<Category> categories = categoryService.getCategories(from, size);
        return new ResponseEntity<>(categoryMapper.toCategoryDtos(categories), HttpStatus.OK);
    }
}
