package ru.practicum.ewm_service.config.exception;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.practicum.ewm_service.error.category.CategoryNotFoundException;
import ru.practicum.ewm_service.error.compilation.CompilationNotFoundException;
import ru.practicum.ewm_service.error.event.EventNotFoundException;
import ru.practicum.ewm_service.error.location.LocationNotFoundException;
import ru.practicum.ewm_service.error.request.RequestStatusException;
import ru.practicum.ewm_service.error.user.UserNotFoundException;

import javax.persistence.EntityNotFoundException;
import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice("ru.practicum.ewm_service")
public class GlobalExceptionController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleThrowable(final Throwable ex, WebRequest request) {
        return ApiError.builder()
                .errors(List.of(ex.getClass().getName()))
                .message(ex.getLocalizedMessage())
                .reason("Непредвиденная ошибка: " + request.getDescription(false))
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleThrowableDataIntegrityViolation(final DataIntegrityViolationException ex, WebRequest request) {
        return ApiError.builder()
                .errors(List.of(ex.getClass().getName()))
                .message(ex.getLocalizedMessage())
                .reason("Нарушение целостности SQL данных: " + request.getDescription(false))
                .status(HttpStatus.CONFLICT)
                .build();
    }

    @ExceptionHandler(value = {
            CategoryNotFoundException.class,
            UserNotFoundException.class,
            LocationNotFoundException.class,
            EventNotFoundException.class,
            CompilationNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleEntityNotFoundException(final EntityNotFoundException ex, WebRequest request) {
        return ApiError.builder()
                .errors(List.of(ex.getClass().getName()))
                .message(ex.getLocalizedMessage())
                .reason("Ошибка поданных данных: " + request.getDescription(false))
                .status(HttpStatus.NOT_FOUND)
                .build();
    }

    @ExceptionHandler(value = {
            RequestStatusException.class
    })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleForbiddenException(final Throwable ex, WebRequest request) {
        return ApiError.builder()
                .errors(List.of(ex.getClass().getName()))
                .message(ex.getLocalizedMessage())
                .reason("Доступ к ресурсу запрещен: " + request.getDescription(false))
                .status(HttpStatus.FORBIDDEN)
                .build();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleServletException(final ServletException ex, WebRequest request) {
        return ApiError.builder()
                .errors(List.of(ex.getClass().getName()))
                .message(ex.getLocalizedMessage())
                .reason("В запросе не указанны обязательные параметры: " + request.getDescription(false))
                .status(HttpStatus.CONFLICT)
                .build();
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleOnMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(e -> errors.add(e.getField() + ": " + e.getDefaultMessage()));
        ex.getBindingResult().getGlobalErrors().forEach(e -> errors.add(e.getObjectName() + ": " + e.getDefaultMessage()));
        return ApiError.builder()
                .errors(errors)
                .message(ex.getLocalizedMessage())
                .reason("Ошибка валидации входящих данных: " + request.getDescription(false))
                .status(HttpStatus.BAD_REQUEST)
                .build();
    }
}
