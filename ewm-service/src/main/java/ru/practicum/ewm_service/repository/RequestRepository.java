package ru.practicum.ewm_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm_service.entity.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    boolean existsByEventIdAndRequesterId(Long eventId, Long requesterId);

    Integer findAllByEventId(Long eventId);

    Optional<Request> findRequestByIdAndRequesterId(Long requestId, Long requesterId);

    List<Request> findAllByRequesterId(Long requesterId);

    List<Request> findAllByEventIdAndEvent_InitiatorId(Long eventId, Long initiatorId);

    @Modifying(clearAutomatically = true)
    @Query(value = "" +
            "UPDATE requests " +
            "SET status = 'CANCELED' " +
            "WHERE event = ?1 AND status = 'PENDING' ", nativeQuery = true)
    void updateCanceledAllRequest(Long eventId);
}
