package ru.practicum.ewm_service.web.admins;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_service.entity.Category;
import ru.practicum.ewm_service.entity.mapper.CategoryMapper;
import ru.practicum.ewm_service.entity.model.category.CategoryDto;
import ru.practicum.ewm_service.service.admin.AdminCategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    private final AdminCategoryService adminCategoryService;
    private final CategoryMapper categoriesMapper;

    @Autowired
    public AdminCategoryController(AdminCategoryService adminCategoryService, CategoryMapper categoriesMapper) {
        this.adminCategoryService = adminCategoryService;
        this.categoriesMapper = categoriesMapper;
    }

    @PostMapping()
    public ResponseEntity<CategoryDto> createCategory(
            @Valid @RequestBody final CategoryDto dto
    ) {
        Category category = adminCategoryService.createCategory(categoriesMapper.toCategory(dto));
        return new ResponseEntity<>(categoriesMapper.toCategoryDto(category), HttpStatus.OK);
    }

    @PatchMapping()
    public ResponseEntity<CategoryDto> updateCategory(
            @Valid @RequestBody final CategoryDto dto
    ) {
        Category category = adminCategoryService.updateCategory(categoriesMapper.toCategory(dto));
        return new ResponseEntity<>(categoriesMapper.toCategoryDto(category), HttpStatus.OK);
    }

    @DeleteMapping("{categoryId}")
    public ResponseEntity<CategoryDto> deleteCategory(
            @PathVariable("categoryId") final long id
    ) {
        adminCategoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }
}

