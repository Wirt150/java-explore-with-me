package ru.practicum.ewm_service.error.event;

import javax.persistence.EntityNotFoundException;

public class EventApproveNotFound extends EntityNotFoundException {
    public EventApproveNotFound(Long id) {
        super(String.format("Событие для публикации с id: %s найдено.", id));
    }

}
