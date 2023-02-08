package ru.practicum.ewm_service.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.ewm_service.entity.Comment;
import ru.practicum.ewm_service.entity.constant.CommentState;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@ActiveProfiles("test")
@SpringBootTest(
        properties = {
                "spring.datasource.driverClassName=org.h2.Driver",
                "spring.datasource.url=jdbc:h2:mem:ewm_service;",
                "spring.datasource.username=test",
                "spring.datasource.password=test"
        },
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@Sql(scripts = {
        "classpath:schema.sql",
        "classpath:sql_scripts/sql_scripts_user_create.sql",
        "classpath:sql_scripts/sql_scripts_category_create.sql",
        "classpath:sql_scripts/sql_scripts_location_create.sql",
        "classpath:sql_scripts/sql_scripts_event_create.sql",
        "classpath:sql_scripts/sql_scripts_request_create.sql",
        "classpath:sql_scripts/sql_scripts_comment_create.sql"},
        executionPhase = BEFORE_TEST_METHOD)
public class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Test
    @Transactional
    @DisplayName("Проверяем кастомный метод deleteByIdAndCreatorIdAndEventId репозитория CommentRepository")
    void deleteByIdAndCreatorIdAndEventIdMethodTest() {
        commentRepository.deleteByIdAndCreatorIdAndEventId(1L, 1L, 1L);

        Optional<Comment> test = commentRepository.findById(1L);

        //test
        assertThat(test, notNullValue());
        assertThat(test.isPresent(), equalTo(false));
    }

    @Test
    @DisplayName("Проверяем кастомный метод findCommentByIdAndEventIdAndStatus репозитория CommentRepository")
    void findCommentByIdAndEventIdAndStatusMethodTest() {
        Comment test = commentRepository.findCommentByIdAndEventIdAndStatus(1L, 1L, CommentState.PENDING);

        //test
        assertThat(test.getId(), equalTo(1L));
        assertThat(test.getEvent(), notNullValue());
        assertThat(test.getCreator(), notNullValue());
        assertThat(test.getText(), equalTo("test"));
        assertThat(test.getCreateOn(), equalTo(Timestamp.valueOf("2022-01-01 00:00:00.00")));
        assertThat(test.getStatus(), equalTo(CommentState.PENDING));
    }

    @Test
    @DisplayName("Проверяем кастомный метод findAllByEventIdAndStatusOrderByIdDesc репозитория CommentRepository")
    void findAllByEventIdAndStatusOrderByIdDescMethodTest() {
        List<Comment> request = commentRepository.findAllByEventIdAndStatusOrderByIdDesc(1L, CommentState.PENDING, PageRequest.of(0, 10));

        //test
        assertThat(request, hasSize(1));

        Comment test = request.get(0);

        //test
        assertThat(test.getId(), equalTo(1L));
        assertThat(test.getEvent(), notNullValue());
        assertThat(test.getCreator(), notNullValue());
        assertThat(test.getText(), equalTo("test"));
        assertThat(test.getCreateOn(), equalTo(Timestamp.valueOf("2022-01-01 00:00:00.00")));
        assertThat(test.getStatus(), equalTo(CommentState.PENDING));
    }
}