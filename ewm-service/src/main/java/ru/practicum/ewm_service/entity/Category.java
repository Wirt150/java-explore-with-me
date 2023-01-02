package ru.practicum.ewm_service.entity;

import lombok.*;

import javax.persistence.*;

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
    @Column(name = "name")
    private String name;
}
