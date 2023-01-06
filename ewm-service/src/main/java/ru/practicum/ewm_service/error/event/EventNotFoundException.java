package ru.practicum.ewm_service.error.event;

import javax.persistence.EntityNotFoundException;

public class EventNotFoundException extends EntityNotFoundException {
    public EventNotFoundException(Long id) {
        super(String.format("Событие с id: %s не найдено.", id));
    }

}
