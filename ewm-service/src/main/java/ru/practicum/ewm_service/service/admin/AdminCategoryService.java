package ru.practicum.ewm_service.service.admin;

import ru.practicum.ewm_service.entity.Category;
import ru.practicum.ewm_service.service.CategoryService;

public interface AdminCategoryService extends CategoryService {
    Category createCategory(Category category);

    Category updateCategory(Category category);

    void deleteCategory(Long catId);
}
