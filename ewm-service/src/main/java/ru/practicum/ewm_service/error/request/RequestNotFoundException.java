package ru.practicum.ewm_service.error.request;

import javax.persistence.EntityNotFoundException;

public class RequestNotFoundException extends EntityNotFoundException {
    public RequestNotFoundException(Long reqId) {
        super(String.format("Реквеста с id: %s не найдено", reqId));
    }
}
