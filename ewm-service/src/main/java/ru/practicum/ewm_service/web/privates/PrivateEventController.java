package ru.practicum.ewm_service.web.privates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_service.entity.Event;
import ru.practicum.ewm_service.entity.mapper.EventMapper;
import ru.practicum.ewm_service.entity.mapper.RequestMapper;
import ru.practicum.ewm_service.entity.model.event.request.EventNewDto;
import ru.practicum.ewm_service.entity.model.event.request.EventUpdateRequest;
import ru.practicum.ewm_service.entity.model.event.response.EventFullDto;
import ru.practicum.ewm_service.entity.model.event.response.EventShortDto;
import ru.practicum.ewm_service.entity.model.request.RequestDto;
import ru.practicum.ewm_service.service.EventService;
import ru.practicum.ewm_service.service.RequestsService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {
    private final EventService eventService;
    private final RequestsService requestsService;
    private final EventMapper eventMapper;
    private final RequestMapper requestMapper;

    @Autowired
    public PrivateEventController(EventService eventService, EventMapper eventMapper, RequestsService requestsService, RequestMapper requestMapper) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
        this.requestsService = requestsService;
        this.requestMapper = requestMapper;
    }

    @PostMapping()
    public ResponseEntity<EventFullDto> createEvent(
            @PathVariable final Long userId,
            @Valid @RequestBody final EventNewDto dto) {
        Event event = eventService.createEvent(eventMapper.toEvent(dto), userId);
        return new ResponseEntity<>(eventMapper.toEventFullDto(event), HttpStatus.OK);
    }

    @PatchMapping()
    public ResponseEntity<EventFullDto> patchEvent(
            @PathVariable final Long userId,
            @Valid @RequestBody final EventUpdateRequest dto) {
        Event event = eventService.updateEvent(dto, userId);
        return new ResponseEntity<>(eventMapper.toEventFullDto(event), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> cancelEvent(
            @PathVariable final Long userId,
            @PathVariable final Long eventId
    ) {
        Event event = eventService.cancelEvent(userId, eventId);
        return new ResponseEntity<>(eventMapper.toEventFullDto(event), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<EventShortDto>> findAllEventByUser(
            @RequestParam(defaultValue = "0") final int from,
            @RequestParam(defaultValue = "10") final int size,
            @PathVariable final Long userId
    ) {
        return new ResponseEntity<>(
                eventMapper.toEventShortDtos(eventService.findAllEventByUserPage(userId, from, size)), HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> findEventByUser(
            @PathVariable final Long userId,
            @PathVariable final Long eventId
    ) {
        return new ResponseEntity<>(eventMapper.toEventFullDto(eventService.findEventByUser(userId, eventId)), HttpStatus.OK);
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<RequestDto>> findRequestByEventUser(
            @PathVariable final Long userId,
            @PathVariable final Long eventId
    ) {
        return new ResponseEntity<>(requestMapper.toRequestDtos(requestsService.findRequestByEventUser(userId, eventId)), HttpStatus.OK);
    }

    @PatchMapping("{eventId}/requests/{reqId}/confirm")
    public ResponseEntity<RequestDto> confirmRequest(
            @PathVariable final Long userId,
            @PathVariable final Long eventId,
            @PathVariable final Long reqId
    ) {
        return new ResponseEntity<>(requestMapper.toRequestDto(requestsService.confirmRequest(userId, eventId, reqId)), HttpStatus.OK);
    }

    @PatchMapping("{eventId}/requests/{reqId}/reject")
    public ResponseEntity<RequestDto> rejectRequest(
            @PathVariable final Long userId,
            @PathVariable final Long eventId,
            @PathVariable final Long reqId
    ) {
        return new ResponseEntity<>(requestMapper.toRequestDto(requestsService.rejectRequest(userId, eventId, reqId)), HttpStatus.OK);
    }


}
