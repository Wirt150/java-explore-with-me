package ru.practicum.ewm_service.web.admins;

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
import ru.practicum.ewm_service.entity.constant.EventState;
import ru.practicum.ewm_service.entity.mapper.CommentMapper;
import ru.practicum.ewm_service.entity.mapper.EventMapper;
import ru.practicum.ewm_service.entity.model.comment.response.CommentResponseDto;
import ru.practicum.ewm_service.entity.model.event.request.AdminEventSearchRequest;
import ru.practicum.ewm_service.entity.model.event.request.AdminUpdateEventRequest;
import ru.practicum.ewm_service.entity.model.event.response.EventFullDto;
import ru.practicum.ewm_service.service.admin.AdminCommentService;
import ru.practicum.ewm_service.service.admin.AdminEventService;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/events")
@Tag(name = "Контроллер событий (Админ)", description = "Управление событиями и комметариями администратором")
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
public class AdminEventController {
    private final AdminEventService adminEventService;
    private final AdminCommentService adminCommentService;
    private final EventMapper eventMapper;
    private final CommentMapper commentMapper;

    @Autowired
    public AdminEventController(AdminEventService eventAdminService, AdminCommentService adminCommentService,
                                EventMapper eventMapper, CommentMapper commentMapper) {
        this.adminEventService = eventAdminService;
        this.adminCommentService = adminCommentService;
        this.eventMapper = eventMapper;
        this.commentMapper = commentMapper;
    }

    @Operation(
            summary = "Поиск событий с различными параметрами (Admin)",
            description = "Ищет и возвращает списиок событий согласно параметрам"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возращает псотранично найденный список",
            content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EventFullDto.class))
                    )
            })
    @GetMapping
    public ResponseEntity<List<EventFullDto>> eventSearch(
            @Parameter(description = "Число страниц") @RequestParam(defaultValue = "0", required = false) final int from,
            @Parameter(description = "Количество обьекторв") @RequestParam(defaultValue = "10", required = false) final int size,
            @Parameter(description = "Id пользователей") @RequestParam(value = "users", required = false) final Set<Long> users,
            @Parameter(description = "Статус события") @RequestParam(value = "states", required = false) final EventState state,
            @Parameter(description = "Id категоий") @RequestParam(value = "categories", required = false) final Set<Long> categories,
            @Parameter(description = "Дата начала события") @RequestParam(value = "rangeStart", required = false) final Timestamp rangeStart,
            @Parameter(description = "Дата конца события") @RequestParam(value = "rangeEnd", required = false) final Timestamp rangeEnd
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

    @Operation(
            summary = "Изменение события (Admin)",
            description = "Изменение полей события администратором"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возращает измененное событие",
            content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EventFullDto.class))
                    )
            })
    @PutMapping("/{eventId}")
    public ResponseEntity<EventFullDto> editEvent(
            @Parameter(description = "Id события") @PathVariable final Long eventId,
            @Parameter(description = "Dto изменяемого события") @RequestBody final AdminUpdateEventRequest adminUpdateEventRequest
    ) {
        Event event = adminEventService.editEvent(adminUpdateEventRequest, eventId);
        return new ResponseEntity<>(eventMapper.toEventFullDto(event), HttpStatus.OK);
    }

    @Operation(
            summary = "Публикация события (Admin)",
            description = "объектов на опубликовано события администратором"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возращает опуликованое событие",
            content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EventFullDto.class))
                    )
            })
    @PatchMapping("/{eventId}/publish")
    public ResponseEntity<EventFullDto> publishEvent(
            @Parameter(description = "Id события") @PathVariable final Long eventId
    ) {
        Event event = adminEventService.publishEvent(eventId);
        return new ResponseEntity<>(eventMapper.toEventFullDto(event), HttpStatus.OK);
    }

    @Operation(
            summary = "Отмена события (Admin)",
            description = "объектов на отменено события администратором"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возращает отмененое событие",
            content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EventFullDto.class))
                    )
            })
    @PatchMapping("/{eventId}/reject")
    public ResponseEntity<EventFullDto> rejectEvent(
            @Parameter(description = "Id события") @PathVariable final Long eventId
    ) {
        Event event = adminEventService.rejectEvent(eventId);
        return new ResponseEntity<>(eventMapper.toEventFullDto(event), HttpStatus.OK);
    }

    @Operation(
            summary = "Закрытие комментария (Admin)",
            description = "объектов на закрыто комментария администратором"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возращает закрытый комментарий",
            content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CommentResponseDto.class))
                    )
            })
    @PatchMapping("/comment/{commentId}/canceled")
    public ResponseEntity<CommentResponseDto> canceledComment(
            @Parameter(description = "Id коментария") @PathVariable final Long commentId
    ) {
        Comment comment = adminCommentService.canceledComment(commentId);
        return new ResponseEntity<>(commentMapper.toCommentResponseDto(comment), HttpStatus.OK);
    }

    @Operation(
            summary = "Публикация комментария (Admin)",
            description = "объектов на опубликовано комментария администратором"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возращает опубликованный комментарий",
            content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CommentResponseDto.class))
                    )
            })
    @PatchMapping("/comment/{commentId}/published")
    public ResponseEntity<CommentResponseDto> publishedComment(
            @Parameter(description = "Id коментария") @PathVariable final Long commentId
    ) {
        Comment comment = adminCommentService.publishedComment(commentId);
        return new ResponseEntity<>(commentMapper.toCommentResponseDto(comment), HttpStatus.OK);
    }

    @Operation(
            summary = "Удаление комментария (Admin)",
            description = "Удаление комментария администратором"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Удаляет комментарий и ничего не возвращает"
    )
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity deleteComment(
            @Parameter(description = "Id коментария") @PathVariable final Long commentId
    ) {
        adminCommentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }
}
