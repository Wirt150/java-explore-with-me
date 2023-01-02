package ru.practicum.ewm_service.entity.model.event.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm_service.entity.Category;
import ru.practicum.ewm_service.entity.model.user.UserShortDto;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {
    private Long id;
    private String annotation;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Category category;
    private Integer confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Timestamp eventDate;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private UserShortDto initiator;
    private boolean paid;
    private String title;
    private Integer views;
}
