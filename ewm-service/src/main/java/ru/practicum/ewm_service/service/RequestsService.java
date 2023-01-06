package ru.practicum.ewm_service.service;

import ru.practicum.ewm_service.entity.Request;

import java.util.List;

public interface RequestsService {
    Request createRequest(Long userId, Long eventId);

    Request cancelRequest(Long userId, Long requestId);

    List<Request> getRequestByUser(Long userId);

    List<Request> findRequestByEventUser(Long userId, Long eventId);

    Request confirmRequest(Long userId, Long eventId, Long reqId);

    Request rejectRequest(Long userId, Long eventId, Long reqId);
}
