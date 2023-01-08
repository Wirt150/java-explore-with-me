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
import ru.practicum.ewm_service.entity.mapper.RequestMapper;
import ru.practicum.ewm_service.entity.model.request.RequestDto;
import ru.practicum.ewm_service.service.RequestsService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@Tag(name = "Контроллер реквестов (Приватный)", description = "Управление реквестами пользователями")
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
public class PrivateRequestsController {

    private final RequestsService requestsService;
    private final RequestMapper requestMapper;

    @Autowired
    public PrivateRequestsController(RequestsService requestsService, RequestMapper requestMapper) {
        this.requestsService = requestsService;
        this.requestMapper = requestMapper;
    }

    @Operation(
            summary = "Создание нового реквеста на событие",
            description = "Создает новый реквест и присваивает id"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возвращает созданный реквест",
            content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RequestDto.class))
                    )
            })
    @PostMapping
    public ResponseEntity<RequestDto> createRequest(
            @Parameter(description = "Id пользователя") @PathVariable final Long userId,
            @Parameter(description = "Id события") @RequestParam final Long eventId
    ) {
        return new ResponseEntity<>(requestMapper.toRequestDto(requestsService.createRequest(userId, eventId)), HttpStatus.OK);
    }

    @Operation(
            summary = "Отмена реквеста на событие",
            description = "Изменение поля статсуа реквеста на отмененный"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возвращает отмененый реквест",
            content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RequestDto.class))
                    )
            })
    @PatchMapping("{requestId}/cancel")
    public ResponseEntity<RequestDto> cancelRequest(
            @Parameter(description = "Id пользователя") @PathVariable final Long userId,
            @Parameter(description = "Id реквеста") @PathVariable final Long requestId
    ) {
        return new ResponseEntity<>(requestMapper.toRequestDto(requestsService.cancelRequest(userId, requestId)), HttpStatus.OK);
    }

    @Operation(
            summary = "Поиск реквестов пользовтеля",
            description = "Ищет все реквесты потльзователя"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возвращает список реквестов пользовтеля",
            content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RequestDto.class))
                    )
            })
    @GetMapping
    public ResponseEntity<List<RequestDto>> getRequestByUser(
            @Parameter(description = "Id пользователя") @PathVariable final Long userId
    ) {
        return new ResponseEntity<>(requestMapper.toRequestDtos(requestsService.getRequestByUser(userId)), HttpStatus.OK);
    }
}
