package ru.practicum.stats_server.entity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatsInfoResponse {
    private String app;
    private String uri;
    private Integer hits;
}
