package ru.practicum.stats_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.stats_server.entity.StatsInfo;
import ru.practicum.stats_server.repository.custom.StatsInfoRepositoryCustom;

public interface StatsInfoRepository extends JpaRepository<StatsInfo, Long>, StatsInfoRepositoryCustom {

}
