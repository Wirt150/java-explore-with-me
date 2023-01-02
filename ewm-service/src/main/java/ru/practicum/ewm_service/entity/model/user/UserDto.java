package ru.practicum.ewm_service.entity.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @Id
    private Long id;
    @NotBlank(message = "Имя не может быть пустым.")
    private String name;
    @NotBlank(message = "Электронная почта не может быть пустой.")
    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Электронная почта должна соответствовать формату RFC 5322.")
    private String email;
}
