package ru.practicum.ewm_service.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.ewm_service.entity.constant.CommentState;
import ru.practicum.ewm_service.entity.model.comment.request.CommentRequestDto;
import ru.practicum.ewm_service.entity.model.comment.response.CommentResponseDto;
import ru.practicum.ewm_service.entity.model.user.UserShortDto;

import java.sql.Timestamp;
import java.time.Instant;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class CommentTest {

    @Autowired
    private JacksonTester<Comment> json;
    @Autowired
    private JacksonTester<CommentRequestDto> jsonReq;
    @Autowired
    private JacksonTester<CommentResponseDto> jsonResp;

    @Test
    @DisplayName("Проверяем правильность сериализации Comment.")
    void whenCreateCommentAndSerializableHim() throws Exception {

        final Comment comment = Comment.builder()
                .id(1L)
                .event(Event.builder().id(1L).build())
                .creator(User.builder().id(1L).build())
                .text("test")
                .createOn(Timestamp.from(Instant.now()))
                .status(CommentState.PENDING)
                .build();

        JsonContent<Comment> result = json.write(comment);

        //test
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.event").extracting("id").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.creator").extracting("id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("test");
        assertThat(result).extractingJsonPathStringValue("$.createOn").isNotBlank();
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(CommentState.PENDING.toString());
    }

    @Test
    @DisplayName("Проверяем правильность сериализации CommentRequestDto.")
    void whenCreateCommentRequestDtoAndSerializableHim() throws Exception {

        final CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .text("test")
                .createOn(Timestamp.from(Instant.now()))
                .status(CommentState.PENDING)
                .build();

        JsonContent<CommentRequestDto> result = jsonReq.write(commentRequestDto);

        //test
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("test");
        assertThat(result).extractingJsonPathStringValue("$.createOn").isNotBlank();
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(CommentState.PENDING.toString());
    }

    @Test
    @DisplayName("Проверяем правильность сериализации CommentResponseDto.")
    void whenCreateCommentResponseDtoAndSerializableHim() throws Exception {

        final CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                .id(1L)
                .creator(UserShortDto.builder().id(1L).build())
                .text("test")
                .createOn(Timestamp.from(Instant.now()))
                .status(CommentState.PENDING)
                .build();

        JsonContent<CommentResponseDto> result = jsonResp.write(commentResponseDto);

        //test
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.creator").extracting("id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("test");
        assertThat(result).extractingJsonPathStringValue("$.createOn").isNotBlank();
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(CommentState.PENDING.toString());
    }
}
