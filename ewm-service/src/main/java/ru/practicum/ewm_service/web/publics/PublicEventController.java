package ru.practicum.ewm_service.web.publics;

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
import ru.practicum.ewm_service.entity.constant.SortState;
import ru.practicum.ewm_service.entity.mapper.CommentMapper;
import ru.practicum.ewm_service.entity.mapper.EventMapper;
import ru.practicum.ewm_service.entity.model.comment.response.CommentResponseDto;
import ru.practicum.ewm_service.entity.model.event.request.PublicEventSearchRequest;
import ru.practicum.ewm_service.entity.model.event.response.EventFullDto;
import ru.practicum.ewm_service.entity.model.event.response.EventShortDto;
import ru.practicum.ewm_service.service.CommentService;
import ru.practicum.ewm_service.service.EventService;
import ru.practicum.ewm_service.web.StatsServerClient;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/events")
@Tag(name = "Контроллер событий (Публичный)", description = "Поиск событий и комметариев неавторизованными пользователями")
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
public class PublicEventController {

    private final StatsServerClient statsServerClient;
    private final EventService eventService;
    private final CommentService commentService;
    private final EventMapper eventMapper;
    private final CommentMapper commentMapper;

    @Autowired
    public PublicEventController(EventService eventService, EventMapper eventMapper, StatsServerClient statsServerClient,
                                 CommentService commentService, CommentMapper commentMapper) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
        this.statsServerClient = statsServerClient;
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    @Operation(
            summary = "Поиск определенного события",
            description = "Ищет событие по id"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возвращает найденое событие",
            content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EventFullDto.class))
                    )
            })
    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEventId(
            @Parameter(description = "Id события") @PathVariable(value = "eventId") final long eventId,
            @Parameter(description = "Информация о HTTP запросе") final HttpServletRequest request
    ) {
        statsServerClient.createHit(request);
        Event event = eventService.findEvent(eventId, PublicEventController.class.getSimpleName());
        return new ResponseEntity<>(eventMapper.toEventFullDto(event), HttpStatus.OK);
    }

    @Operation(
            summary = "Поиск событий с различными параметрами",
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
    @GetMapping
    public ResponseEntity<List<EventShortDto>> eventSearch(
            @Parameter(description = "Число страниц") @RequestParam(defaultValue = "0", required = false) final int from,
            @Parameter(description = "Количество обьекторв") @RequestParam(defaultValue = "10", required = false) final int size,
            @Parameter(description = "Название или описание события") @RequestParam(value = "text", required = false) final String text,
            @Parameter(description = "Id категоий") @RequestParam(value = "categories", required = false) final Set<Long> categories,
            @Parameter(description = "Платное или бесплатное") @RequestParam(value = "paid", required = false) final boolean paid,
            @Parameter(description = "Статус доступности регистрации") @RequestParam(value = "onlyAvailable", required = false) final boolean onlyAvailable,
            @Parameter(description = "Способ сортировки") @RequestParam(value = "sort", required = false) final SortState sort,
            @Parameter(description = "Дата начала события") @RequestParam(value = "rangeStart", required = false) final Timestamp rangeStart,
            @Parameter(description = "Дата конца события") @RequestParam(value = "rangeEnd", required = false) final Timestamp rangeEnd,
            @Parameter(description = "Информация о HTTP запросе") final HttpServletRequest request
    ) {
        statsServerClient.createHit(request);
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

    @Operation(
            summary = "Поиск определенного комментария к событию",
            description = "Ищет комментарий по id"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возвращает найденый комментарий",
            content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CommentResponseDto.class))
                    )
            })
    @GetMapping("/{eventId}/comment/{commentId}")
    public ResponseEntity<CommentResponseDto> getComment(
            @Parameter(description = "Id события") @PathVariable final Long eventId,
            @Parameter(description = "Id комментария") @PathVariable final Long commentId,
            @Parameter(description = "Информация о HTTP запросе") final HttpServletRequest request
    ) {
        statsServerClient.createHit(request);
        Comment comment = commentService.getCommentsById(eventId, commentId);
        return new ResponseEntity<>(commentMapper.toCommentResponseDto(comment), HttpStatus.OK);
    }

    @Operation(
            summary = "Поиск всех комментариев к событию",
            description = "Ищет и возвращает списиок событий согласно параметрам "
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возращает псотранично найденный список",
            content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CommentResponseDto.class))
                    )
            })
    @GetMapping("/{eventId}/comment")
    public ResponseEntity<List<CommentResponseDto>> getComments(
            @Parameter(description = "Id события") @PathVariable final Long eventId,
            @Parameter(description = "Число страниц") @RequestParam(defaultValue = "0") final int from,
            @Parameter(description = "Количество объектов") @RequestParam(defaultValue = "10") final int size,
            @Parameter(description = "Информация о HTTP запросе") final HttpServletRequest request
    ) {
        statsServerClient.createHit(request);
        List<Comment> comments = commentService.getComments(eventId, from, size);
        return new ResponseEntity<>(commentMapper.toCommentResponseDtos(comments), HttpStatus.OK);
    }
}
