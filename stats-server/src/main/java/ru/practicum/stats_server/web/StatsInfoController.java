package ru.practicum.stats_server.web;

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
import ru.practicum.stats_server.entity.StatsInfo;
import ru.practicum.stats_server.entity.mapper.StatsInfoMapper;
import ru.practicum.stats_server.entity.model.StatsInfoDto;
import ru.practicum.stats_server.entity.model.StatsInfoRequest;
import ru.practicum.stats_server.entity.model.StatsInfoResponse;
import ru.practicum.stats_server.service.StatsService;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping()
@Tag(name = "Контроллер доступа к серверу статистики", description = "Запрос и сохранение статистики")
@ApiResponses(
        value = {
                @ApiResponse(responseCode = "200", description = "Создано"),
                @ApiResponse(responseCode = "400", description = "Ошибка валидации входных данных"),
                @ApiResponse(responseCode = "409", description = "Не уникальное поле в базе данных"),
                @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка"),
        }
)
public class StatsInfoController {

    private final StatsService statsService;
    private final StatsInfoMapper statsInfoMapper;

    @Autowired
    public StatsInfoController(StatsService hitService, StatsInfoMapper statsInfoMapper) {
        this.statsService = hitService;
        this.statsInfoMapper = statsInfoMapper;
    }

    @Operation(
            summary = "Регистрация входящей информации от сервиса",
            description = "Создает новую точку сохранения и присваивает id"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возвращает созданую точку сохранения",
            content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = StatsInfo.class))
                    )
            })
    @PostMapping("/hit")
    public ResponseEntity<StatsInfo> createStatsInfo(
            @Parameter(description = "Dto точки сохранения") @RequestBody final StatsInfoDto dto
    ) {
        StatsInfo statsInfo = statsService.createStatsinfo(statsInfoMapper.toStatsInfo(dto));
        return new ResponseEntity<>(statsInfo, HttpStatus.OK);
    }

    @Operation(
            summary = "Поиск обращений по uri",
            description = "Ищет и возвращает списиок событий согласно параметрам"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возращает псотранично найденный список",
            content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = StatsInfoResponse.class))
                    )
            })
    @GetMapping("/stats")
    public ResponseEntity<List<StatsInfoResponse>> findStats(
            @Parameter(description = "Дата начала поиска информации") @RequestParam(value = "start") final Timestamp start,
            @Parameter(description = "Дата конца поиска информации") @RequestParam(value = "end") final Timestamp end,
            @Parameter(description = "Список uri по которым вести поиск") @RequestParam(value = "uris", required = false) final List<String> uris,
            @Parameter(description = "Только уникальные ip") @RequestParam(value = "unique", defaultValue = "false", required = false) final boolean unique
    ) {
        final StatsInfoRequest statsRequest = StatsInfoRequest.builder()
                .start(start)
                .end(end)
                .uris(uris)
                .unique(unique)
                .build();
        return new ResponseEntity<>(statsService.findStats(statsRequest), HttpStatus.OK);
    }
}
