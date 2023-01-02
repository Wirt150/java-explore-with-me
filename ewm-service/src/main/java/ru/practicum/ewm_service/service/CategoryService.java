package ru.practicum.ewm_service.service;

import ru.practicum.ewm_service.entity.Category;

import java.util.List;

public interface CategoryService {
    Category getCategoryById(Long catId);

    List<Category> getCategories(int from, int size);
}
