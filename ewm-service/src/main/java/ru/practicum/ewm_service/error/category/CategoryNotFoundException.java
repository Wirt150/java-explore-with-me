package ru.practicum.ewm_service.error.category;

import javax.persistence.EntityNotFoundException;

public class CategoryNotFoundException extends EntityNotFoundException {
    public CategoryNotFoundException(Long id) {
        super(String.format("Категория с id: %s не найден.", id));
    }
}
