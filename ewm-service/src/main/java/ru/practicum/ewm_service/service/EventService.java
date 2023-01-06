package ru.practicum.ewm_service.service;

import ru.practicum.ewm_service.entity.Event;
import ru.practicum.ewm_service.entity.model.event.request.EventUpdateRequest;
import ru.practicum.ewm_service.entity.model.event.request.PublicEventSearchRequest;

import java.util.List;

public interface EventService {
    Event createEvent(Event event, Long userId);

    Event updateEvent(EventUpdateRequest eventUpdateRequest, Long userId);

    Event findEvent(Long eventId, String name);

    List<Event> findEvents(List<Long> events);

    List<Event> findAllEventByUserPage(Long userId, int from, int size);

    Event findEventByUser(Long userId, Long eventId);

    Event cancelEvent(Long userId, Long eventId);

    List<Event> eventSearchPublic(PublicEventSearchRequest eventSearchRequest);
}
