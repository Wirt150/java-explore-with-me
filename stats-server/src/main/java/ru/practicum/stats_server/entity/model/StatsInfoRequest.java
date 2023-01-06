package ru.practicum.stats_server.entity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatsInfoRequest {
    private Timestamp start;
    private Timestamp end;
    private List<String> uris;
    private boolean unique;
}
