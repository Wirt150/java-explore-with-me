package ru.practicum.ewm_service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.ewm_service.entity.constant.EventState;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString(of = {"id", "annotation", "title", "state"})
@EqualsAndHashCode(of = {"id", "annotation", "title", "state"})
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "annotation")
    private String annotation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category", referencedColumnName = "id")
    private Category category;
    @Builder.Default
    @Column(name = "confirmed_requests")
    private Integer confirmedRequests = 0;
    @Builder.Default
    @Column(name = "created_on")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Timestamp createdOn = Timestamp.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
    @Column(name = "description")
    private String description;
    @Column(name = "event_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Timestamp eventDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator", referencedColumnName = "id")
    private User initiator;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location", referencedColumnName = "id")
    private Location location;
    @Column(name = "paid")
    private boolean paid;
    @Builder.Default
    @Column(name = "participant_limit")
    private Integer participantLimit = 0;
    @Builder.Default
    @Column(name = "published_on")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Timestamp publishedOn = Timestamp.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
    @Builder.Default
    @Column(name = "request_moderation")
    private boolean requestModeration = true;
    @Builder.Default
    @Column(name = "state", length = 10)
    @Enumerated(EnumType.STRING)
    private EventState state = EventState.PENDING;
    @Column(name = "title")
    private String title;
    @Builder.Default
    @Column(name = "views")
    private Integer views = 0;
}
