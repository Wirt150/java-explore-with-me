package ru.practicum.stats_server.service;

import ru.practicum.stats_server.entity.StatsInfo;
import ru.practicum.stats_server.entity.model.StatsInfoRequest;
import ru.practicum.stats_server.entity.model.StatsInfoResponse;

import java.util.List;

public interface StatsService {
    StatsInfo createStatsinfo(StatsInfo statsInfo);

    List<StatsInfoResponse> findStats(StatsInfoRequest statsRequest);
}
