package ru.practicum.stats_server.repository.custom;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.practicum.stats_server.entity.model.StatsInfoRequest;
import ru.practicum.stats_server.entity.model.StatsInfoResponse;

import javax.transaction.Transactional;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Transactional
@AllArgsConstructor
public class StatsInfoRepositoryCustomImpl implements StatsInfoRepositoryCustom {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<StatsInfoResponse> findStatsInfoResponse(StatsInfoRequest statsRequest) {
        String distinct = "";
        if (statsRequest.isUnique()) {
            distinct = "DISTINCT";
        }
        String sql = String.format("" +
                "SELECT app, uri, count(%s(ip)) AS hits " +
                "FROM stats AS s " +
                "WHERE timestamp BETWEEN :start AND :end AND uri LIKE ANY (ARRAY[ :uris ])" +
                "GROUP BY  uri,  app, ip;", distinct);
        MapSqlParameterSource inQueryParams = new MapSqlParameterSource();
        inQueryParams.addValue("uris", statsRequest.getUris());
        inQueryParams.addValue("start", statsRequest.getStart());
        inQueryParams.addValue("end", statsRequest.getEnd());
        return namedParameterJdbcTemplate.query(sql, inQueryParams, new StatsInfoResponseRowMapper());
    }

    static class StatsInfoResponseRowMapper implements RowMapper<StatsInfoResponse> {
        @Override
        public StatsInfoResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
            return StatsInfoResponse.builder()
                    .app(rs.getString("app"))
                    .uri(rs.getString("uri"))
                    .hits(rs.getInt("hits"))
                    .build();
        }
    }
}