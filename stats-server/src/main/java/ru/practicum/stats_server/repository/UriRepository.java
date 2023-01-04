package ru.practicum.stats_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.stats_server.entity.Uri;

public interface UriRepository extends JpaRepository<Uri, String> {
    boolean existsByName(String name);

    Uri findByName(Uri uri);
}
