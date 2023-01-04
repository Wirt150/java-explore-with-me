package ru.practicum.stats_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.stats_server.entity.StatsInfo;

public interface StatsInfoRepository extends JpaRepository<StatsInfo, Long> {
}
