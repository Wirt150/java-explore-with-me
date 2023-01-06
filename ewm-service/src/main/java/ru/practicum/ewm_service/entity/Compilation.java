package ru.practicum.ewm_service.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@ToString(of = {"id", "pinned", "title"})
@EqualsAndHashCode(of = {"id", "pinned", "title"})
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "pinned")
    private boolean pinned;
    @NotNull
    @Size(max = 120, message = "Максимальный размер окграничен, 120 символов.")
    @Column(name = "title")
    private String title;
    @ManyToMany
    @JoinTable(
            name = "compilations_event",
            joinColumns = @JoinColumn(name = "compilations_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Event> events;
}
