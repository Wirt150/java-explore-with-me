package ru.practicum.ewm_service.service.admin;

import ru.practicum.ewm_service.entity.Comment;
import ru.practicum.ewm_service.service.CommentService;

public interface AdminCommentService extends CommentService {
    Comment canceledComment(Long commentId);

    Comment publishedComment(Long commentId);

    void deleteComment(Long commentId);
}
