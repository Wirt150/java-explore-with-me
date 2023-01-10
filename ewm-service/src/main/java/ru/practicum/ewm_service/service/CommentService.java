package ru.practicum.ewm_service.service;

import ru.practicum.ewm_service.entity.Comment;

import java.util.List;

public interface CommentService {
    Comment createComment(Long userId, Long eventId, Comment comment);

    Comment updateComment(Long userId, Long eventId, Long commentId, Comment comment);

    void deleteCommentByUser(Long userId, Long eventId, Long commentId);

    Comment getCommentsById(Long eventId, Long commentId);

    List<Comment> getComments(Long eventId, int from, int size);
}
