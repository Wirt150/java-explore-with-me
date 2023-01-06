package ru.practicum.ewm_service.entity.model.event.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm_service.entity.constant.SortState;

import java.sql.Timestamp;
import java.util.Set;

@Data
@Builder
public class PublicEventSearchRequest {
    private int fromPage;
    private int sizePage;
    private String text;
    private Set<Long> categories;
    private boolean paid;
    private boolean onlyAvailable;
    private SortState sort;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp rangeStart;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp rangeEnd;
}
