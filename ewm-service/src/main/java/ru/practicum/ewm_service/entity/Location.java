package ru.practicum.ewm_service.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString(of = {"id", "lat", "lon"})
@EqualsAndHashCode(of = {"id", "lat", "lon"})
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "lat")
    private float lat;
    @Column(name = "lon")
    private float lon;
}
