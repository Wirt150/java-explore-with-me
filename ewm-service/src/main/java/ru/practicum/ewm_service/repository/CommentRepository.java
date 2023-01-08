package ru.practicum.ewm_service.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm_service.entity.Comment;
import ru.practicum.ewm_service.entity.constant.CommentState;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    void deleteByIdAndCreatorIdAndEventId(Long commentId, Long creatorId, Long eventId);

    Comment findCommentByIdAndEventIdAndStatus(Long commentId, Long eventId, CommentState commentState);

    List<Comment> findAllByEventIdAndStatusOrderByIdDesc(Long eventId, CommentState commentState, Pageable pageable);
}
