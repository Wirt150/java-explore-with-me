package ru.practicum.ewm_service.entity.model.event.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm_service.entity.constant.EventState;

import java.sql.Timestamp;
import java.util.Set;

@Data
@Builder
public class AdminEventSearchRequest {
    private int fromPage;
    private int sizePage;
    private Set<Long> users;
    private EventState eventState;
    private Set<Long> categories;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Timestamp rangeStart;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Timestamp rangeEnd;
}
