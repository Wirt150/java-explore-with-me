package ru.practicum.ewm_service.error.compilation;

import javax.persistence.EntityNotFoundException;

public class CompilationNotFoundException extends EntityNotFoundException {
    public CompilationNotFoundException(Long id) {
        super(String.format("Подборка с id: %s не найдена.", id));
    }
}


