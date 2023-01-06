package ru.practicum.stats_server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Getter
@Setter
@ToString(of = {"id", "app", "ip"})
@EqualsAndHashCode(of = {"id", "app", "ip"})
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "stats")
public class StatsInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Size(max = 225, message = "Максимальный размер окграничен, 225 символа.")
    @Column(name = "app")
    private String app;
    @NotNull
    @Size(max = 100, message = "Максимальный размер окграничен, 100 символа.")
    @Column(name = "uri")
    private String uri;
    @NotNull
    @Size(max = 20, message = "Максимальный размер окграничен, 20 символа.")
    @Column(name = "ip")
    private String ip;
    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Timestamp timestamp;
}
