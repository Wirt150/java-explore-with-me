package ru.practicum.ewm_service.entity.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm_service.entity.Comment;
import ru.practicum.ewm_service.entity.model.comment.request.CommentRequestDto;
import ru.practicum.ewm_service.entity.model.comment.response.CommentResponseDto;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CommentMapper {
    Comment toComment(CommentRequestDto dto);

    CommentResponseDto toCommentResponseDto(Comment comment);

    List<CommentResponseDto> toCommentResponseDtos(List<Comment> comments);
}
