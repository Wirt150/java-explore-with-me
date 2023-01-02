package ru.practicum.ewm_service.web.publics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_service.entity.Event;
import ru.practicum.ewm_service.entity.constant.SortState;
import ru.practicum.ewm_service.entity.mapper.EventMapper;
import ru.practicum.ewm_service.entity.model.event.request.PublicEventSearchRequest;
import ru.practicum.ewm_service.entity.model.event.response.EventFullDto;
import ru.practicum.ewm_service.entity.model.event.response.EventShortDto;
import ru.practicum.ewm_service.service.EventService;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/events")
public class PublicEventController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @Autowired
    public PublicEventController(EventService eventService, EventMapper eventMapper) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEvent(
            @PathVariable(value = "eventId") final long eventId
    ) {
        Event event = eventService.findEvent(eventId, PublicEventController.class.getSimpleName());
        return new ResponseEntity<>(eventMapper.toEventFullDto(event), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EventShortDto>> eventSearch(
            @RequestParam(defaultValue = "0", required = false) final int from,
            @RequestParam(defaultValue = "10", required = false) final int size,
            @RequestParam(value = "text", required = false) final String text,
            @RequestParam(value = "categories", required = false) final Set<Long> categories,
            @RequestParam(value = "paid", required = false) final boolean paid,
            @RequestParam(value = "onlyAvailable", required = false) final boolean onlyAvailable,
            @RequestParam(value = "sort", required = false) final SortState sort,
            @RequestParam(value = "rangeStart", required = false) final Timestamp rangeStart,
            @RequestParam(value = "rangeEnd", required = false) final Timestamp rangeEnd
    ) {
        PublicEventSearchRequest eventSearchRequest = PublicEventSearchRequest.builder()
                .fromPage(from)
                .sizePage(size)
                .text(text)
                .categories(categories)
                .paid(paid)
                .onlyAvailable(onlyAvailable)
                .sort(sort)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .build();
        return new ResponseEntity<>(eventMapper.toEventShortDtos(eventService.eventSearchPublic(eventSearchRequest)), HttpStatus.OK);
    }
}
