package ru.practicum.ewm_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm_service.entity.Event;
import ru.practicum.ewm_service.entity.constant.EventState;
import ru.practicum.ewm_service.entity.model.event.request.AdminEventSearchRequest;
import ru.practicum.ewm_service.entity.model.event.request.AdminUpdateEventRequest;
import ru.practicum.ewm_service.entity.model.event.request.EventUpdateRequest;
import ru.practicum.ewm_service.entity.model.event.request.PublicEventSearchRequest;
import ru.practicum.ewm_service.error.event.EventApproveNotFound;
import ru.practicum.ewm_service.error.event.EventNotFoundException;
import ru.practicum.ewm_service.repository.EventRepository;
import ru.practicum.ewm_service.service.CategoryService;
import ru.practicum.ewm_service.service.EventService;
import ru.practicum.ewm_service.service.LocationService;
import ru.practicum.ewm_service.service.UserService;
import ru.practicum.ewm_service.service.admin.AdminEventService;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class EventServiceImpl implements EventService, AdminEventService {

    private static final int ONE_HOUR = 1;
    private final EventRepository eventRepository;
    private final LocationService locationService;
    private final UserService userService;
    private final CategoryService categoryService;

    @Override
    public Event createEvent(final Event event, final Long userId) {
        event.setCategory(categoryService.getCategoryById(event.getCategory().getId()));
        event.setInitiator(userService.getById(userId));
        event.setLocation(locationService.create(event.getLocation()));
        return eventRepository.save(event);
    }

    @Override
    public Event updateEvent(final EventUpdateRequest eventUpdateRequest, final Long userId) {
        final Event eventUpdate = eventRepository.findByIdAndInitiatorIdAndStateNot(eventUpdateRequest.getEventId(), userId, EventState.CANCELED).orElseThrow(() -> new EventNotFoundException(eventUpdateRequest.getEventId()));
        if (EventState.CANCELED.equals(eventUpdate.getState())) {
            eventUpdate.setState(EventState.PENDING);
        }
        Optional.ofNullable(eventUpdateRequest.getAnnotation()).ifPresent(eventUpdate::setAnnotation);
        Optional.ofNullable(eventUpdateRequest.getCategory()).ifPresent(i -> eventUpdate.setCategory(categoryService.getCategoryById(i)));
        Optional.ofNullable(eventUpdateRequest.getDescription()).ifPresent(eventUpdate::setDescription);
        Optional.ofNullable(eventUpdateRequest.getEventDate()).ifPresent(eventUpdate::setEventDate);
        Optional.of(eventUpdateRequest.isPaid()).ifPresent(eventUpdate::setPaid);
        Optional.ofNullable(eventUpdateRequest.getParticipantLimit()).ifPresent(eventUpdate::setParticipantLimit);
        Optional.ofNullable(eventUpdateRequest.getTitle()).ifPresent(eventUpdate::setTitle);
        return eventRepository.save(eventUpdate);
    }

    @Override
    public Event findEvent(final Long eventId, final String name) {
        if (name.equals("PublicEventController")) {
            Event findEvent = eventRepository.findEventByIdAndState(eventId, EventState.PUBLISHED)
                    .orElseThrow(() -> new EventNotFoundException(eventId));
            findEvent.setViews(findEvent.getViews() + 1);
            return findEvent;
        }
        return eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
    }

    @Override
    public List<Event> findEvents(List<Long> events) {
        return eventRepository.findAllById(events);
    }

    @Override
    public List<Event> findAllEventByUserPage(final Long userId, final int from, final int size) {
        return eventRepository.findAllEventByInitiatorId(userId, PageRequest.of(from, size));
    }

    @Override
    public Event findEventByUser(final Long userId, final Long eventId) {
        return eventRepository.findEventByIdAndInitiatorId(eventId, userId);
    }

    @Override
    public Event cancelEvent(final Long userId, final Long eventId) {
        final Event eventUpdate = eventRepository.findByIdAndInitiatorIdAndState(eventId, userId, EventState.PENDING).orElseThrow(() -> new EventNotFoundException(eventId));
        eventUpdate.setState(EventState.CANCELED);
        return eventRepository.save(eventUpdate);
    }

    @Override
    public List<Event> eventSearchPublic(final PublicEventSearchRequest eventSearchRequest) {
        List<Event> events = eventRepository.eventPublicSearch(eventSearchRequest);
        events.forEach(event -> event.setViews(event.getViews() + 1));
        return events;
    }

    @Override
    public List<Event> eventSearchAdmin(final AdminEventSearchRequest eventSearchRequest) {
        return eventRepository.findAllByInitiatorIdInAndStateAndCategoryIdInAndEventDateBetween(eventSearchRequest.getUsers(), eventSearchRequest.getEventState(), eventSearchRequest.getCategories(), eventSearchRequest.getRangeStart(), eventSearchRequest.getRangeEnd(), PageRequest.of(eventSearchRequest.getFromPage(), eventSearchRequest.getSizePage()));
    }

    @Override
    public Event editEvent(final AdminUpdateEventRequest adminUpdateEventRequest, final Long eventId) {
        final Event eventUpdate = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        Optional.ofNullable(adminUpdateEventRequest.getAnnotation()).ifPresent(eventUpdate::setAnnotation);
        Optional.ofNullable(adminUpdateEventRequest.getCategory()).ifPresent(i -> eventUpdate.setCategory(categoryService.getCategoryById(i)));
        Optional.ofNullable(adminUpdateEventRequest.getDescription()).ifPresent(eventUpdate::setDescription);
        Optional.ofNullable(adminUpdateEventRequest.getEventDate()).ifPresent(eventUpdate::setEventDate);
        Optional.ofNullable(adminUpdateEventRequest.getLocation()).ifPresent(l -> eventUpdate.setLocation(locationService.create(l)));
        Optional.of(adminUpdateEventRequest.isPaid()).ifPresent(eventUpdate::setPaid);
        Optional.ofNullable(adminUpdateEventRequest.getParticipantLimit()).ifPresent(eventUpdate::setParticipantLimit);
        Optional.of(adminUpdateEventRequest.isRequestModeration()).ifPresent(eventUpdate::setRequestModeration);
        Optional.ofNullable(adminUpdateEventRequest.getTitle()).ifPresent(eventUpdate::setTitle);
        return eventRepository.save(eventUpdate);
    }

    @Override
    public Event publishEvent(final Long eventId) {
        Timestamp timestampValid = Timestamp.valueOf(LocalDateTime.now().plusHours(ONE_HOUR));
        Event eventPublish = eventRepository.findEventByIdAndStateAndEventDateAfter(eventId, EventState.PENDING, timestampValid).orElseThrow(() -> new EventApproveNotFound(eventId));
        eventPublish.setState(EventState.PUBLISHED);
        return eventRepository.save(eventPublish);
    }

    @Override
    public Event rejectEvent(final Long eventId) {
        Event eventReject = eventRepository.findEventByIdAndStateNot(eventId, EventState.CANCELED).orElseThrow(() -> new EventApproveNotFound(eventId));
        eventReject.setState(EventState.CANCELED);
        return eventRepository.save(eventReject);
    }
}
