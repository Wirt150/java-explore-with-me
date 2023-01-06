package ru.practicum.ewm_service.entity.model.event.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm_service.entity.Category;
import ru.practicum.ewm_service.entity.Location;
import ru.practicum.ewm_service.entity.constant.EventState;
import ru.practicum.ewm_service.entity.model.user.UserShortDto;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {
    private Long id;
    private String annotation;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Category category;
    private Integer confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createdOn;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp eventDate;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private UserShortDto initiator;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Location location;
    private boolean paid;
    private Integer participantLimit;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp publishedOn;
    private boolean requestModeration;
    private EventState state;
    private String title;
    private Integer views;
}
