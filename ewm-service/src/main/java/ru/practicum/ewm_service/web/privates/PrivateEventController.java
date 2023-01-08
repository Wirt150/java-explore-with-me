package ru.practicum.ewm_service.web.privates;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_service.entity.Comment;
import ru.practicum.ewm_service.entity.Event;
import ru.practicum.ewm_service.entity.mapper.CommentMapper;
import ru.practicum.ewm_service.entity.mapper.EventMapper;
import ru.practicum.ewm_service.entity.mapper.RequestMapper;
import ru.practicum.ewm_service.entity.model.comment.request.CommentRequestDto;
import ru.practicum.ewm_service.entity.model.comment.response.CommentResponseDto;
import ru.practicum.ewm_service.entity.model.event.request.EventNewDto;
import ru.practicum.ewm_service.entity.model.event.request.EventUpdateRequest;
import ru.practicum.ewm_service.entity.model.event.response.EventFullDto;
import ru.practicum.ewm_service.entity.model.event.response.EventShortDto;
import ru.practicum.ewm_service.entity.model.request.RequestDto;
import ru.practicum.ewm_service.service.CommentService;
import ru.practicum.ewm_service.service.EventService;
import ru.practicum.ewm_service.service.RequestsService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@Tag(name = "Контроллер событий (Приватный)", description = "Управление событиями и комметариями пользователями")
@ApiResponses(
        value = {
                @ApiResponse(responseCode = "200", description = "Создано"),
                @ApiResponse(responseCode = "400", description = "Ошибка валидации входных данных"),
                @ApiResponse(responseCode = "404", description = "Не найденный объект"),
                @ApiResponse(responseCode = "403", description = "Доступ к ресурсу рграничен"),
                @ApiResponse(responseCode = "409", description = "Не уникальное поле в базе данных"),
                @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка"),
        }
)
public class PrivateEventController {
    private final EventService eventService;
    private final RequestsService requestsService;
    private final CommentService commentsService;
    private final EventMapper eventMapper;
    private final RequestMapper requestMapper;
    private final CommentMapper commentMapper;

    @Autowired
    public PrivateEventController(EventService eventService, EventMapper eventMapper, RequestsService requestsService,
                                  CommentService commentsService, RequestMapper requestMapper, CommentMapper commentMapper) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
        this.requestsService = requestsService;
        this.commentsService = commentsService;
        this.requestMapper = requestMapper;
        this.commentMapper = commentMapper;
    }

    @Operation(
            summary = "Создание нового события",
            description = "Создает новое событие и присваивает id"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возвращает созданное новое событие",
            content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EventFullDto.class))
                    )
            })
    @PostMapping()
    public ResponseEntity<EventFullDto> createEvent(
            @Parameter(description = "Id пользователя") @PathVariable final Long userId,
            @Parameter(description = "Dto события") @Valid @RequestBody final EventNewDto dto) {
        Event event = eventService.createEvent(eventMapper.toEvent(dto), userId);
        return new ResponseEntity<>(eventMapper.toEventFullDto(event), HttpStatus.OK);
    }

    @Operation(
            summary = "Изменение события",
            description = "Изменение полей события пользователем"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возращает измененное событие",
            content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EventFullDto.class))
                    )
            })
    @PatchMapping()
    public ResponseEntity<EventFullDto> patchEvent(
            @Parameter(description = "Id пользователя") @PathVariable final Long userId,
            @Parameter(description = "Dto события") @Valid @RequestBody final EventUpdateRequest dto) {
        Event event = eventService.updateEvent(dto, userId);
        return new ResponseEntity<>(eventMapper.toEventFullDto(event), HttpStatus.OK);
    }

    @Operation(
            summary = "Отмена события",
            description = "Изменение поля статсуа на отменено события пользователем"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возращает отмененое событие",
            content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EventFullDto.class))
                    )
            })
    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> cancelEvent(
            @Parameter(description = "Id пользователя") @PathVariable final Long userId,
            @Parameter(description = "Id события") @PathVariable final Long eventId
    ) {
        Event event = eventService.cancelEvent(userId, eventId);
        return new ResponseEntity<>(eventMapper.toEventFullDto(event), HttpStatus.OK);
    }

    @Operation(
            summary = "Поиск всех событий у пользователя",
            description = "Ищет и возвращает списиок событий согласно параметрам"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возращает псотранично найденный список",
            content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EventShortDto.class))
                    )
            })
    @GetMapping()
    public ResponseEntity<List<EventShortDto>> findAllEventByUser(
            @Parameter(description = "Число страниц") @RequestParam(defaultValue = "0") final int from,
            @Parameter(description = "Количество объектов") @RequestParam(defaultValue = "10") final int size,
            @Parameter(description = "Id пользователя") @PathVariable final Long userId
    ) {
        return new ResponseEntity<>(
                eventMapper.toEventShortDtos(eventService.findAllEventByUserPage(userId, from, size)), HttpStatus.OK);
    }

    @Operation(
            summary = "Поиск события у пользователя",
            description = "Ищет и возвращает событие пользователя"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возращает событие у пользователя",
            content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EventFullDto.class))
                    )
            })
    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> findEventByUser(
            @Parameter(description = "Id пользователя") @PathVariable final Long userId,
            @Parameter(description = "Id события") @PathVariable final Long eventId
    ) {
        return new ResponseEntity<>(eventMapper.toEventFullDto(eventService.findEventByUser(userId, eventId)), HttpStatus.OK);
    }

    @Operation(
            summary = "Поиск реквестов на событие пользователя",
            description = "Ищет все реквесты на событие пользователя"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возвращает список реквестов на событие",
            content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RequestDto.class))
                    )
            })
    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<RequestDto>> findRequestByEventUser(
            @Parameter(description = "Id пользователя") @PathVariable final Long userId,
            @Parameter(description = "Id события") @PathVariable final Long eventId
    ) {
        return new ResponseEntity<>(requestMapper.toRequestDtos(requestsService.findRequestByEventUser(userId, eventId)), HttpStatus.OK);
    }

    @Operation(
            summary = "Подтверждение реквеста пользователем",
            description = "Изменение поля статсуа на подтвержденое реквеста на событие"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возращает подтвержденый реквест",
            content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RequestDto.class))
                    )
            })
    @PatchMapping("{eventId}/requests/{reqId}/confirm")
    public ResponseEntity<RequestDto> confirmRequest(
            @Parameter(description = "Id пользователя") @PathVariable final Long userId,
            @Parameter(description = "Id события") @PathVariable final Long eventId,
            @Parameter(description = "Id реквеста") @PathVariable final Long reqId
    ) {
        return new ResponseEntity<>(requestMapper.toRequestDto(requestsService.confirmRequest(userId, eventId, reqId)), HttpStatus.OK);
    }

    @Operation(
            summary = "Отклонение реквеста пользователем",
            description = "Изменение поля статсуа на отклонено реквеста на событие"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возращает отклоненный реквест",
            content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RequestDto.class))
                    )
            })
    @PatchMapping("{eventId}/requests/{reqId}/reject")
    public ResponseEntity<RequestDto> rejectRequest(
            @Parameter(description = "Id пользователя") @PathVariable final Long userId,
            @Parameter(description = "Id события") @PathVariable final Long eventId,
            @Parameter(description = "Id реквеста") @PathVariable final Long reqId
    ) {
        return new ResponseEntity<>(requestMapper.toRequestDto(requestsService.rejectRequest(userId, eventId, reqId)), HttpStatus.OK);
    }

    @Operation(
            summary = "Создание нового комментария к событию пользователем",
            description = "Создает нововый комментарий и присваивает id"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возвращает созданный комментарий",
            content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CommentResponseDto.class))
                    )
            })
    @PostMapping("/{eventId}/comment")
    public ResponseEntity<CommentResponseDto> createComment(
            @Parameter(description = "Id пользователя") @PathVariable final Long userId,
            @Parameter(description = "Id события") @PathVariable final Long eventId,
            @Parameter(description = "Dto комметраия") @Valid @RequestBody final CommentRequestDto dto
    ) {
        Comment comment = commentsService.createComment(userId, eventId, commentMapper.toComment(dto));
        return new ResponseEntity<>(commentMapper.toCommentResponseDto(comment), HttpStatus.OK);
    }

    @Operation(
            summary = "Изменение комментария к событию пользователем",
            description = "Менияет текст комментария у пользователя"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возвращает измененный комментарий",
            content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CommentResponseDto.class))
                    )
            })
    @PatchMapping("/{eventId}/comment/{commentId}")
    public ResponseEntity<CommentResponseDto> patchComment(
            @Parameter(description = "Id пользователя") @PathVariable final Long userId,
            @Parameter(description = "Id события") @PathVariable final Long eventId,
            @Parameter(description = "Id комментария") @PathVariable final Long commentId,
            @Parameter(description = "Dto комментария") @Valid @RequestBody final CommentRequestDto dto
    ) {
        Comment comment = commentsService.updateComment(userId, eventId, commentId, commentMapper.toComment(dto));
        return new ResponseEntity<>(commentMapper.toCommentResponseDto(comment), HttpStatus.OK);
    }

    @Operation(
            summary = "Удаляет комментарий пользователя к событию",
            description = "Удаление своего комментария"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Удаляет комментарий и ничего не возвращает"
    )
    @DeleteMapping("/{eventId}/comment/{commentId}")
    public ResponseEntity deleteComment(
            @Parameter(description = "Id пользователя") @PathVariable final Long userId,
            @Parameter(description = "Id события") @PathVariable final Long eventId,
            @Parameter(description = "Id комментария") @PathVariable final Long commentId
    ) {
        commentsService.deleteCommentByUser(userId, eventId, commentId);
        return ResponseEntity.ok().build();
    }


}
