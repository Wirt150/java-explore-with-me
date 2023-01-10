package ru.practicum.ewm_service.error.comment;

public class CommentStatusException extends RuntimeException {
    public CommentStatusException() {
        super("Нет доступа к изменению этого комментария.");
    }


}
