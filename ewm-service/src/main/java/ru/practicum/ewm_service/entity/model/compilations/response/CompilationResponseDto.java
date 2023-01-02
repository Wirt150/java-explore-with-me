package ru.practicum.ewm_service.entity.model.compilations.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm_service.entity.model.event.response.EventShortDto;

import javax.persistence.Id;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationResponseDto {
    @Id
    private Long id;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<EventShortDto> events;
    private boolean pinned;
    private String title;
}
