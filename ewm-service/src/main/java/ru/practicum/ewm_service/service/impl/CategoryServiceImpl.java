package ru.practicum.ewm_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm_service.entity.Category;
import ru.practicum.ewm_service.error.category.CategoryExistEventException;
import ru.practicum.ewm_service.error.category.CategoryNotFoundException;
import ru.practicum.ewm_service.repository.CategoryRepository;
import ru.practicum.ewm_service.repository.EventRepository;
import ru.practicum.ewm_service.service.CategoryService;
import ru.practicum.ewm_service.service.admin.AdminCategoryService;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService, AdminCategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public Category createCategory(final Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(final Long catId) {
        return categoryRepository.findById(catId).orElseThrow(() -> new CategoryNotFoundException(catId));
    }

    @Override
    public List<Category> getCategories(int from, int size) {
        return categoryRepository.findAll(PageRequest.of(from, size)).getContent();
    }

    @Override
    public Category updateCategory(final Category category) {
        final Category categoryUpdate = categoryRepository
                .findById(category.getId()).orElseThrow(() -> new CategoryNotFoundException(category.getId()));
        categoryUpdate.setName(category.getName());
        return categoryRepository.save(categoryUpdate);
    }

    @Override
    public void deleteCategory(final Long catId) {
        if (eventRepository.existsByCategoryId(catId)) {
            throw new CategoryExistEventException(catId);
        }
        categoryRepository.deleteById(catId);
    }
}
