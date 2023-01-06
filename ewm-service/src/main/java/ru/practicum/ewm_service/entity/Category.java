package ru.practicum.ewm_service.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString(of = {"id", "name"})
@EqualsAndHashCode(of = {"id", "name"})
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Size(max = 255, message = "Максимальный размер окграничен, 255 символов.")
    @Column(name = "name")
    private String name;
}
