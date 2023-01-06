package ru.practicum.ewm_service.entity.model.server;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Hit {
    private String app;
    private String uri;
    private String ip;
    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp timestamp = Timestamp.from(Instant.now());
}

