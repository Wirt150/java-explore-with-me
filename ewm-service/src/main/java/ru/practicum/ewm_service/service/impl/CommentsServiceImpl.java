package ru.practicum.ewm_service.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm_service.entity.Comment;
import ru.practicum.ewm_service.entity.constant.CommentState;
import ru.practicum.ewm_service.error.comment.CommentNotFoundException;
import ru.practicum.ewm_service.error.comment.CommentStatusException;
import ru.practicum.ewm_service.repository.CommentRepository;
import ru.practicum.ewm_service.service.CommentService;
import ru.practicum.ewm_service.service.EventService;
import ru.practicum.ewm_service.service.UserService;
import ru.practicum.ewm_service.service.admin.AdminCommentService;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class CommentsServiceImpl implements CommentService, AdminCommentService {

    private final CommentRepository commentRepository;
    private final EventService eventService;
    private final UserService userService;

    @Override
    public Comment createComment(final Long userId, final Long eventId, final Comment comment) {
        comment.setCreator(userService.getById(userId));
        comment.setEvent(eventService.findEvent(eventId, CommentsServiceImpl.class.getSimpleName()));
        return commentRepository.save(comment);
    }

    @Override
    public Comment updateComment(final Long userId, final Long eventId, final Long commentId, final Comment comment) {
        Comment commentUpdate = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException(commentId));
        if (!commentUpdate.getCreator().getId().equals(userId)) throw new CommentStatusException();
        eventService.findEvent(eventId, CommentsServiceImpl.class.getSimpleName());
        commentUpdate.setText(comment.getText());
        if (commentUpdate.getStatus().equals(CommentState.CANCELED)) commentUpdate.setStatus(CommentState.PENDING);
        return commentUpdate;
    }

    @Override
    public void deleteCommentByUser(final Long userId, final Long eventId, final Long commentId) {
        commentRepository.deleteByIdAndCreatorIdAndEventId(commentId, userId, eventId);
    }

    @Override
    public Comment getCommentsById(final Long eventId, final Long commentId) {
        return commentRepository.findCommentByIdAndEventIdAndStatus(commentId, eventId, CommentState.PUBLISHED);
    }

    @Override
    public List<Comment> getComments(final Long eventId, final int from, final int size) {
        return commentRepository.findAllByEventIdAndStatusOrderByCreateOnDesc(eventId, CommentState.PUBLISHED, PageRequest.of(from, size));
    }

    @Override
    public Comment canceledComment(final Long commentId) {
        Comment commentUpdate = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException(commentId));
        commentUpdate.setStatus(CommentState.CANCELED);
        return commentUpdate;
    }

    @Override
    public Comment publishedComment(final Long commentId) {
        Comment commentUpdate = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException(commentId));
        commentUpdate.setStatus(CommentState.PUBLISHED);
        return commentUpdate;
    }

    @Override
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
