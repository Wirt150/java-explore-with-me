package ru.practicum.ewm_service.error.location;

import javax.persistence.EntityNotFoundException;

public class LocationNotFoundException extends EntityNotFoundException {
    public LocationNotFoundException(Long id) {
        super(String.format("Локация с id: %s не найдена.", id));
    }
}
