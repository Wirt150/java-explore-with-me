package ru.practicum.ewm_service.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString(of = {"id", "name", "email"})
@EqualsAndHashCode(of = {"id", "name", "email"})
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Size(max = 255, message = "Максимальный размер окграничен, 255 символов.")
    @Column(name = "name")
    private String name;
    @NotNull
    @Size(max = 64, message = "Максимальный размер окграничен, 64 символа.")
    @Column(name = "email", length = 512)
    private String email;
}