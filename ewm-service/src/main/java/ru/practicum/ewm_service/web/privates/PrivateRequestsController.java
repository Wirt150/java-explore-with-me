package ru.practicum.ewm_service.web.privates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_service.entity.mapper.RequestMapper;
import ru.practicum.ewm_service.entity.model.request.RequestDto;
import ru.practicum.ewm_service.service.RequestsService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
public class PrivateRequestsController {

    private final RequestsService requestsService;
    private final RequestMapper requestMapper;

    @Autowired
    public PrivateRequestsController(RequestsService requestsService, RequestMapper requestMapper) {
        this.requestsService = requestsService;
        this.requestMapper = requestMapper;
    }

    @PostMapping
    public ResponseEntity<RequestDto> createRequest(
            @PathVariable final Long userId,
            @RequestParam(value = "eventId") final Long eventId
    ) {
        return new ResponseEntity<>(requestMapper.toRequestDto(requestsService.createRequest(userId, eventId)), HttpStatus.OK);
    }

    @PatchMapping("{requestId}/cancel")
    public ResponseEntity<RequestDto> cancelRequest(
            @PathVariable final Long userId,
            @PathVariable final Long requestId
    ) {
        return new ResponseEntity<>(requestMapper.toRequestDto(requestsService.cancelRequest(userId, requestId)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<RequestDto>> getRequestByUser(
            @PathVariable final Long userId
    ) {
        return new ResponseEntity<>(requestMapper.toRequestDtos(requestsService.getRequestByUser(userId)), HttpStatus.OK);
    }
}
