package ru.practicum.ewm_service.error.request;

public class RequestStatusException extends RuntimeException {
    public RequestStatusException() {
        super("Не удалось изменить статус заявки заявку.");
    }
}
