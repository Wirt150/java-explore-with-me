package ru.practicum.ewm_service.entity.model.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    @Id
    private Long id;
    @NotBlank(message = "Наименование категории не может быть пустым.")
    private String name;
}
