package ru.practicum.ewm_service.error.comment;

import javax.persistence.EntityNotFoundException;

public class CommentNotFoundException extends EntityNotFoundException {
    public CommentNotFoundException(Long id) {
        super(String.format("Комментария с id: %s не найдено.", id));
    }
}
