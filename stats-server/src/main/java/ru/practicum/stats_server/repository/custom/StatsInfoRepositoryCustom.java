package ru.practicum.stats_server.repository.custom;

import ru.practicum.stats_server.entity.model.StatsInfoRequest;
import ru.practicum.stats_server.entity.model.StatsInfoResponse;

import java.util.List;

public interface StatsInfoRepositoryCustom {
    List<StatsInfoResponse> findStatsInfoResponse(StatsInfoRequest statsRequest);

}