package ru.practicum.stats_server.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import ru.practicum.stats_server.entity.StatsInfo;
import ru.practicum.stats_server.entity.mapper.StatsInfoResponseRowMapper;
import ru.practicum.stats_server.entity.model.StatsInfoRequest;
import ru.practicum.stats_server.entity.model.StatsInfoResponse;
import ru.practicum.stats_server.repository.StatsInfoRepository;
import ru.practicum.stats_server.repository.UriRepository;
import ru.practicum.stats_server.service.StatsService;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final StatsInfoRepository statsInfoRepository;
    private final UriRepository uriRepository;
    private final StatsInfoResponseRowMapper statsInfoResponseRowMapper;

    @Override
    public StatsInfo createStatsinfo(final StatsInfo statsInfo) {
        if (!uriRepository.existsByName(statsInfo.getUri().getName())) {
            statsInfo.setUri(uriRepository.save(statsInfo.getUri()));
        } else {
            statsInfo.setUri(uriRepository.findByName(statsInfo.getUri()));
        }
        return statsInfoRepository.save(statsInfo);
    }

    @Override
    public List<StatsInfoResponse> findStats(StatsInfoRequest statsRequest) {

        MapSqlParameterSource inQueryParams = new MapSqlParameterSource();
        inQueryParams.addValue("uris", statsRequest.getUris().);
        inQueryParams.addValue("start", statsRequest.getStart());
        inQueryParams.addValue("end", statsRequest.getEnd());

        List<StatsInfoResponse> statsInfoResponse =
                namedParameterJdbcTemplate.query("" +
                        "SELECT u.app_name AS app, u.name AS uri, count(distinct(ip)) AS hits " +
                        "FROM stats AS s " +
                        "JOIN uris u on u.name = s.uri " +
                        "WHERE timestamp BETWEEN :start AND :end AND uri LIKE (:uris) " +
                        "GROUP BY s.uri, u.name, u.app_name", inQueryParams, statsInfoResponseRowMapper);

        return statsInfoResponse;
    }
}
