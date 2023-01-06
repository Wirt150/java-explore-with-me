package ru.practicum.ewm_service.web.admins;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_service.entity.Event;
import ru.practicum.ewm_service.entity.constant.EventState;
import ru.practicum.ewm_service.entity.mapper.EventMapper;
import ru.practicum.ewm_service.entity.model.event.request.AdminEventSearchRequest;
import ru.practicum.ewm_service.entity.model.event.request.AdminUpdateEventRequest;
import ru.practicum.ewm_service.entity.model.event.response.EventFullDto;
import ru.practicum.ewm_service.service.admin.AdminEventService;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/events")
public class AdminEventController {
    private final AdminEventService adminEventService;
    private final EventMapper eventMapper;

    @Autowired
    public AdminEventController(AdminEventService eventAdminService, EventMapper eventMapper) {
        this.adminEventService = eventAdminService;
        this.eventMapper = eventMapper;
    }

    @GetMapping
    public ResponseEntity<List<EventFullDto>> eventSearch(
            @RequestParam(defaultValue = "0", required = false) final int from,
            @RequestParam(defaultValue = "10", required = false) final int size,
            @RequestParam(value = "users", required = false) final Set<Long> users,
            @RequestParam(value = "states", required = false) final EventState state,
            @RequestParam(value = "categories", required = false) final Set<Long> categories,
            @RequestParam(value = "rangeStart", required = false) final Timestamp rangeStart,
            @RequestParam(value = "rangeEnd", required = false) final Timestamp rangeEnd
    ) {
        AdminEventSearchRequest eventSearchRequest = AdminEventSearchRequest.builder()
                .fromPage(from)
                .sizePage(size)
                .users(users)
                .eventState(state)
                .categories(categories)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .build();
        return new ResponseEntity<>(eventMapper.toEventFullDtos(adminEventService.eventSearchAdmin(eventSearchRequest)), HttpStatus.OK);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventFullDto> editEvent(
            @PathVariable final Long eventId,
            @RequestBody final AdminUpdateEventRequest adminUpdateEventRequest
    ) {
        Event event = adminEventService.editEvent(adminUpdateEventRequest, eventId);
        return new ResponseEntity<>(eventMapper.toEventFullDto(event), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}/publish")
    public ResponseEntity<EventFullDto> publishEvent(
            @PathVariable final Long eventId
    ) {
        Event event = adminEventService.publishEvent(eventId);
        return new ResponseEntity<>(eventMapper.toEventFullDto(event), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}/reject")
    public ResponseEntity<EventFullDto> rejectEvent(
            @PathVariable final Long eventId
    ) {
        Event event = adminEventService.rejectEvent(eventId);
        return new ResponseEntity<>(eventMapper.toEventFullDto(event), HttpStatus.OK);
    }

}
