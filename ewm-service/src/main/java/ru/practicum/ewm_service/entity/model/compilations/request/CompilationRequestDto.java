package ru.practicum.ewm_service.entity.model.compilations.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationRequestDto {
    private Set<Long> events;
    @Builder.Default
    private boolean pinned = false;
    @NotBlank(message = "Заголовок не может быть пустым.")
    private String title;
}
