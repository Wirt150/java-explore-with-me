package ru.practicum.ewm_service.error.request;

public class DuplicationRequestException extends RuntimeException {
    public DuplicationRequestException(Long eventId, Long userId) {
        super(String.format("Повторный запрос на событие id: %s от пользователя c id: %s.", eventId, userId));
    }
}
