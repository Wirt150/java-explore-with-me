package ru.practicum.ewm_service.entity;

import lombok.*;
import ru.practicum.ewm_service.entity.constant.CommentState;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@ToString(of = {"id", "text", "status"})
@EqualsAndHashCode(of = {"id", "text", "status"})
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event", referencedColumnName = "id")
    private Event event;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator", referencedColumnName = "id")
    private User creator;
    @Column(name = "text")
    private String text;
    @Column(name = "create_on")
    private Timestamp createOn;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CommentState status;
}
