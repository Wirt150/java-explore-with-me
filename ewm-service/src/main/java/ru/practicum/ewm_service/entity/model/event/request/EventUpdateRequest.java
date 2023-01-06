package ru.practicum.ewm_service.entity.model.event.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm_service.config.validator.ValidateDate;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventUpdateRequest {
    @Id
    @Positive(message = "Id события не может быть отрицательным.")
    private Long eventId;
    @Size(min = 20, max = 2000, message = "Описание события: 20 ... 2000 символов.")
    @NotBlank(message = "Описание события не может быть пустым.")
    private String annotation;
    private Long category;
    @Size(min = 20, max = 7000, message = "Полное описание события: 20 ... 7000 символов.")
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @ValidateDate(message = "Дата и время на которые намечено событие не может быть раньше, чем через 2 часа от текущего момента.")
    private Timestamp eventDate;
    private boolean paid;
    private Integer participantLimit;
    @Size(min = 3, max = 120, message = "Заголовок события: 3 ... 120 символов.")
    private String title;
}
