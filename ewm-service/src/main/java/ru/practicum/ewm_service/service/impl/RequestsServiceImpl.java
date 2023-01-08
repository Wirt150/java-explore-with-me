package ru.practicum.ewm_service.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm_service.entity.Event;
import ru.practicum.ewm_service.entity.Request;
import ru.practicum.ewm_service.entity.constant.EventState;
import ru.practicum.ewm_service.entity.constant.RequestStatus;
import ru.practicum.ewm_service.error.request.DuplicationRequestException;
import ru.practicum.ewm_service.error.request.RequestNotCreateException;
import ru.practicum.ewm_service.error.request.RequestNotFoundException;
import ru.practicum.ewm_service.error.request.RequestStatusException;
import ru.practicum.ewm_service.repository.RequestRepository;
import ru.practicum.ewm_service.service.EventService;
import ru.practicum.ewm_service.service.RequestsService;
import ru.practicum.ewm_service.service.UserService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@AllArgsConstructor
public class RequestsServiceImpl implements RequestsService {

    private final RequestRepository requestRepository;
    private final EventService eventService;
    private final UserService userService;

    @Override
    public Request createRequest(final Long userId, final Long eventId) {
        if (requestRepository.existsByEventIdAndRequesterId(eventId, userId))
            throw new DuplicationRequestException(eventId, userId);

        Event event = eventService.findEvent(eventId, RequestsServiceImpl.class.getSimpleName());

        if (!event.getState().equals(EventState.PUBLISHED) && Objects.equals(event.getParticipantLimit(),
                requestRepository.findAllByEventId(eventId)) && Objects.equals(event.getInitiator().getId(), userId)) {
            throw new RequestNotCreateException(eventId, userId);
        }

        return requestRepository.save(Request.builder().event(event).requester(userService.getById(userId))
                .status(event.isRequestModeration() ? RequestStatus.PENDING : RequestStatus.CONFIRMED).build());
    }

    @Override
    public Request cancelRequest(final Long userId, final Long requestId) {
        Request requestCancel = requestRepository.findRequestByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new RequestNotFoundException(requestId));
        requestCancel.setStatus(RequestStatus.CANCELED);
        return requestRepository.save(requestCancel);
    }

    @Override
    public List<Request> getRequestByUser(final Long userId) {
        return requestRepository.findAllByRequesterId(userId);
    }

    @Override
    public List<Request> findRequestByEventUser(final Long userId, final Long eventId) {
        return requestRepository.findAllByEventIdAndEvent_InitiatorId(eventId, userId);
    }

    @Override
    public Request confirmRequest(Long userId, Long eventId, Long reqId) {
        Event event = eventService.findEvent(eventId, RequestsServiceImpl.class.getSimpleName());
        Request request = requestRepository.findById(reqId).orElseThrow(() -> new RequestNotFoundException(reqId));
        if (!event.isRequestModeration() || event.getParticipantLimit() == 0 ||
                Objects.equals(event.getConfirmedRequests(), event.getParticipantLimit())) {
            throw new RequestStatusException();
        }
        request.setStatus(RequestStatus.CONFIRMED);
        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        if (Objects.equals(event.getConfirmedRequests(), event.getParticipantLimit())) {
            requestRepository.updateCanceledAllRequest(eventId);
        }
        return request;
    }

    @Override
    public Request rejectRequest(Long userId, Long eventId, Long reqId) {
        Event event = eventService.findEvent(eventId, RequestsServiceImpl.class.getSimpleName());
        Request request = requestRepository.findById(reqId).orElseThrow(() -> new RequestNotFoundException(reqId));

        if (Objects.equals(event.getInitiator().getId(), userId) && Objects.equals(request.getEvent().getId(), event.getId())) {
            request.setStatus(RequestStatus.REJECTED);
            return request;
        }
        throw new RequestStatusException();
    }
}
