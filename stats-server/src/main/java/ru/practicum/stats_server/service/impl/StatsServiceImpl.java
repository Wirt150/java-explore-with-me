package ru.practicum.stats_server.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.stats_server.entity.StatsInfo;
import ru.practicum.stats_server.entity.model.StatsInfoRequest;
import ru.practicum.stats_server.entity.model.StatsInfoResponse;
import ru.practicum.stats_server.repository.StatsInfoRepository;
import ru.practicum.stats_server.service.StatsService;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsInfoRepository statsInfoRepository;

    @Override
    public StatsInfo createStatsinfo(final StatsInfo statsInfo) {
        return statsInfoRepository.save(statsInfo);
    }

    @Override
    public List<StatsInfoResponse> findStats(StatsInfoRequest statsRequest) {
        return statsInfoRepository.findStatsInfoResponse(statsRequest);
    }
}
