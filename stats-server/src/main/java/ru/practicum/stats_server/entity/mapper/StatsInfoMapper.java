package ru.practicum.stats_server.entity.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.stats_server.entity.StatsInfo;
import ru.practicum.stats_server.entity.Uri;
import ru.practicum.stats_server.entity.model.StatsInfoDto;

@Component
public class StatsInfoMapper {
    public StatsInfo toStatsInfo(StatsInfoDto dto) {
        return StatsInfo.builder()
                .uri(Uri.builder().name(dto.getUri()).appName(dto.getApp()).build())
                .ip(dto.getIp())
                .timestamp(dto.getTimestamp())
                .build();
    }
}
