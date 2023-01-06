package ru.practicum.ewm_service.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm_service.entity.Compilation;

import java.util.List;

@Repository
public interface CompilationsRepository extends JpaRepository<Compilation, Long> {
    @Modifying(clearAutomatically = true)
    @Query(value = "" +
            "DELETE FROM compilations_event " +
            "WHERE compilations_id = ?1 AND event_id = ?2", nativeQuery = true)
    void deleteEventsCompilations(long comId, long eventId);

    @Modifying(clearAutomatically = true)
    @Query(value = "" +
            "INSERT INTO compilations_event (compilations_id, event_id)" +
            "VALUES (?1, ?2) ", nativeQuery = true)
    void editEventsCompilations(long comId, long eventId);

    @Modifying(clearAutomatically = true)
    @Query(value = "" +
            "UPDATE compilations " +
            "SET pinned = false " +
            "WHERE id = ?1", nativeQuery = true)
    void deletePinCompilations(long compId);

    @Modifying(clearAutomatically = true)
    @Query(value = "" +
            "UPDATE compilations " +
            "SET pinned = true " +
            "WHERE id = ?1", nativeQuery = true)
    void editPinCompilations(long compId);

    List<Compilation> findAllByPinned(boolean pinned, Pageable pageable);
}
