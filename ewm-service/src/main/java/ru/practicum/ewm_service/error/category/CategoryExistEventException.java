package ru.practicum.ewm_service.error.category;

public class CategoryExistEventException extends RuntimeException {
    public CategoryExistEventException(Long id) {
        super(String.format("Категория с id: %s привязана к событию и не может быть удалена.", id));
    }
}
