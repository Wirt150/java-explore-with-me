package ru.practicum.ewm_service.error.request;

public class RequestNotCreateException extends RuntimeException {
    public RequestNotCreateException(Long eventId, Long userId) {
        super(String.format("Невозможно создать реквест на свое событие.(EventId: %s, UserId: %s)", eventId, userId));
    }

}
