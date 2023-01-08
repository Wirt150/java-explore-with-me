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
import ru.practicum.ewm_service.entity.Compilation;
import ru.practicum.ewm_service.entity.mapper.CompilationMapper;
import ru.practicum.ewm_service.entity.model.compilations.request.CompilationRequestDto;
import ru.practicum.ewm_service.entity.model.compilations.response.CompilationResponseDto;
import ru.practicum.ewm_service.service.admin.AdminCompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
@Tag(name = "Контроллер подборок (Админ)", description = "Управление подборками событий администратором")
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
public class AdminCompilationsController {

    private final AdminCompilationService adminCompilationService;
    private final CompilationMapper compilationMapper;

    @Autowired
    public AdminCompilationsController(AdminCompilationService adminCompilationService, CompilationMapper compilationMapper) {
        this.adminCompilationService = adminCompilationService;
        this.compilationMapper = compilationMapper;
    }

    @Operation(
            summary = "Создание заголовка для подборок событий (Admin)",
            description = "Создает новый заголовок и присваивает id"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возвращает созданный заголовок",
            content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CompilationResponseDto.class))
                    )
            })
    @PostMapping()
    public ResponseEntity<CompilationResponseDto> createCompilation(
            @Parameter(description = "Dto подборки событий") @Valid @RequestBody final CompilationRequestDto dto
    ) {
        Compilation compilation = adminCompilationService.createCompilation(compilationMapper.toCompilation(dto));
        return new ResponseEntity<>(compilationMapper.toCompilationResponseDto(compilation), HttpStatus.OK);
    }

    @Operation(summary = "Удаление подборки событий по ее id (Admin)")
    @ApiResponse(
            responseCode = "200",
            description = "Удаляет подборки событий и ничего не возвращает"
    )
    @DeleteMapping("{compId}")
    public ResponseEntity deleteCompilation(
            @Parameter(description = "Id подборки событий") @PathVariable("compId") final long compId
    ) {
        adminCompilationService.deleteCompilation(compId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удаление события по его id из подборки событий по ее id (Admin)")
    @ApiResponse(
            responseCode = "200",
            description = "Удаляет событие из подборки событий и ничего не возвращает"
    )
    @DeleteMapping("{compId}/events/{eventId}")
    public ResponseEntity deleteEventCompilation(
            @Parameter(description = "Id подборки событий") @PathVariable("compId") final long compId,
            @Parameter(description = "Id события") @PathVariable("eventId") final long eventId
    ) {
        adminCompilationService.deleteEventCompilation(compId, eventId);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Добавление события по его id в подборку событий по ее id (Admin)",
            description = "Создает новую подборку событий и присваивает id"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Добавляет событие в подборку событий и ничего не возвращает"
    )
    @PatchMapping("{compId}/events/{eventId}")
    public ResponseEntity editEventCompilation(
            @Parameter(description = "Id подборки событий") @PathVariable("compId") final long compId,
            @Parameter(description = "Id события") @PathVariable("eventId") final long eventId
    ) {
        adminCompilationService.editEventCompilation(compId, eventId);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Убирает подборку событий по ее id из закрепленных (Admin)",
            description = "Убирает подборку событий из закрепленной на главной странице"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Убирает подборку событий из закрепленных и ничего не возвращает"
    )
    @DeleteMapping("{compId}/pin")
    public ResponseEntity deletePinCompilation(
            @Parameter(description = "Id подборки событий") @PathVariable("compId") final long compId
    ) {
        adminCompilationService.deletePinCompilation(compId);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Добавление подборки событий по ее id в закрепленные (Admin)",
            description = "Добавляет подборку событий в закрепленные на главной странице"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Добавляет подборку событий в закрепленные и ничего не возвращает"
    )
    @PatchMapping("{compId}/pin")
    public ResponseEntity editPinCompilation(
            @Parameter(description = "Id подборки событий") @PathVariable("compId") final long compId
    ) {
        adminCompilationService.editPinCompilation(compId);
        return ResponseEntity.ok().build();
    }
}
