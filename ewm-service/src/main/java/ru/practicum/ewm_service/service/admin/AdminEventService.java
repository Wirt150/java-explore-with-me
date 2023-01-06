package ru.practicum.ewm_service.service.admin;

import ru.practicum.ewm_service.entity.Event;
import ru.practicum.ewm_service.entity.model.event.request.AdminEventSearchRequest;
import ru.practicum.ewm_service.entity.model.event.request.AdminUpdateEventRequest;
import ru.practicum.ewm_service.service.EventService;

import java.util.List;

public interface AdminEventService extends EventService {

    List<Event> eventSearchAdmin(AdminEventSearchRequest eventSearchRequest);

    Event editEvent(AdminUpdateEventRequest adminUpdateEventRequest, Long eventId);

    Event publishEvent(Long eventId);

    Event rejectEvent(Long eventId);
}
