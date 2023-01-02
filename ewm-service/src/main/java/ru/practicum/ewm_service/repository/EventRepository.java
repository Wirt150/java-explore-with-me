package ru.practicum.ewm_service.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm_service.entity.Event;
import ru.practicum.ewm_service.entity.constant.EventState;
import ru.practicum.ewm_service.repository.custom.EventCustomRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long>, EventCustomRepository {
    boolean existsByCategoryId(Long catId);

    Event findEventByIdAndInitiatorId(Long id, Long initiatorId);

    Optional<Event> findEventByIdAndState(Long eventId, EventState state);

    Optional<Event> findByIdAndInitiatorIdAndStateNot(Long eventId, Long initiatorId, EventState state);

    Optional<Event> findByIdAndInitiatorIdAndState(Long eventId, Long initiatorId, EventState state);

    List<Event> findAllEventByInitiatorId(Long initiatorId, Pageable pageable);

    List<Event> findAllByInitiatorIdInAndStateAndCategoryIdInAndEventDateBetween(
            Set<Long> initiatorId, EventState state, Set<Long> categoryId, Timestamp start, Timestamp end, Pageable pageable);

    Optional<Event> findEventByIdAndStateAndEventDateAfter(Long id, EventState state, Timestamp validHour);

    Optional<Event> findEventByIdAndStateNot(Long id, EventState state);
}
