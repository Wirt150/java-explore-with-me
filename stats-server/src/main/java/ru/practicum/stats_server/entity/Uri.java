package ru.practicum.stats_server.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString(of = {"id"})
@EqualsAndHashCode(of = {"id"})
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "uris")
public class Uri {
    @Id
    @Column(name = "name")
    private String name;
    @Column(name = "app_name")
    private String appName;
}
