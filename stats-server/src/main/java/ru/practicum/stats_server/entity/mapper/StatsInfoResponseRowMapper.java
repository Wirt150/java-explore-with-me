package ru.practicum.stats_server.entity.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.practicum.stats_server.entity.model.StatsInfoResponse;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StatsInfoResponseRowMapper implements RowMapper<StatsInfoResponse> {
    @Override
    public StatsInfoResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        return StatsInfoResponse.builder()
                .app(rs.getString("app"))
                .uri(rs.getString("uri"))
                .hits(rs.getInt("hits"))
                .build();
    }
}
