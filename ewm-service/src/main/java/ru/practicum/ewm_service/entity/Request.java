package ru.practicum.ewm_service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.ewm_service.entity.constant.RequestStatus;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString(of = {"id", "event", "status"})
@EqualsAndHashCode(of = {"id", "event", "status"})
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "created")
    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Timestamp created = Timestamp.valueOf(LocalDateTime.now());
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event", referencedColumnName = "id")
    private Event event;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester", referencedColumnName = "id")
    private User requester;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
}
