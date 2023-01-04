package ru.practicum.stats_server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uri", referencedColumnName = "name")
    private Uri uri;
    @Column(name = "ip")
    private String ip;
    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Timestamp timestamp;
}
