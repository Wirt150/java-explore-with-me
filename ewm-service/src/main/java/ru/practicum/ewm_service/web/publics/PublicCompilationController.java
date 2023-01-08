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
import ru.practicum.ewm_service.entity.Compilation;
import ru.practicum.ewm_service.entity.mapper.CompilationMapper;
import ru.practicum.ewm_service.entity.model.compilations.response.CompilationResponseDto;
import ru.practicum.ewm_service.service.CompilationService;

import java.util.List;

@RestController
@RequestMapping("/compilations")
@Tag(name = "Контроллер подборок (Публичный)", description = "Поиск подборок неавторизованными пользователями")
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
public class PublicCompilationController {

    private final CompilationService compilationService;
    private final CompilationMapper compilationMapper;

    @Autowired
    public PublicCompilationController(CompilationService compilationService, CompilationMapper compilationMapper) {
        this.compilationService = compilationService;
        this.compilationMapper = compilationMapper;
    }

    @Operation(
            summary = "Поиск определенной компиляции",
            description = "Ищет компиляцию по ее id"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возвращает найденую компиляцию",
            content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CompilationResponseDto.class))
                    )
            })
    @GetMapping("/{compId}")
    public ResponseEntity<CompilationResponseDto> getCompilation(
            @Parameter(description = "Id компиляции") @PathVariable(value = "compId") final long compId
    ) {
        Compilation compilation = compilationService.getCompilation(compId);
        return new ResponseEntity<>(compilationMapper.toCompilationResponseDto(compilation), HttpStatus.OK);
    }

    @Operation(
            summary = "Поиск всех компиляций",
            description = "Ищет и возвращает списиок событий согласно параметрам "
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возращает псотранично найденный список",
            content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CompilationResponseDto.class))
                    )
            })
    @GetMapping
    public ResponseEntity<List<CompilationResponseDto>> getCompilations(
            @Parameter(description = "Число страниц") @RequestParam(defaultValue = "0") final int from,
            @Parameter(description = "Количество объектов") @RequestParam(defaultValue = "10") final int size,
            @Parameter(description = "Закрпеленные или нет") @RequestParam(defaultValue = "false") final boolean pinned
    ) {
        List<Compilation> compilations = compilationService.getCompilations(pinned, from, size);
        return new ResponseEntity<>(compilationMapper.toCompilationResponseDtos(compilations), HttpStatus.OK);
    }
}
