package ru.practicum.stats_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.stats_server.entity.StatsInfo;
import ru.practicum.stats_server.repository.custom.StatsInfoRepositoryCustom;

@Repository
public interface StatsInfoRepository extends JpaRepository<StatsInfo, Long>, StatsInfoRepositoryCustom {
}
