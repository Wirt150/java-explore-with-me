package ru.practicum.stats_server.web;

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
public class StatsInfoController {

    private final StatsService statsService;
    private final StatsInfoMapper statsInfoMapper;

    @Autowired
    public StatsInfoController(StatsService hitService, StatsInfoMapper statsInfoMapper) {
        this.statsService = hitService;
        this.statsInfoMapper = statsInfoMapper;
    }

    @PostMapping("/hit")
    public ResponseEntity<StatsInfo> createStatsInfo(
            @RequestBody final StatsInfoDto dto
    ) {
        StatsInfo hit = statsService.createStatsinfo(statsInfoMapper.toStatsInfo(dto));
        return new ResponseEntity<>(hit, HttpStatus.OK);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<StatsInfoResponse>> findStats(
            @RequestParam(value = "start") final Timestamp start,
            @RequestParam(value = "end") final Timestamp end,
            @RequestParam(value = "uris", required = false) final List<String> uris,
            @RequestParam(value = "unique", defaultValue = "false", required = false) final boolean unique
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
