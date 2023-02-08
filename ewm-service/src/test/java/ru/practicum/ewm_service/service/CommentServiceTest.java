package ru.practicum.ewm_service.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.ewm_service.entity.*;
import ru.practicum.ewm_service.entity.constant.CommentState;
import ru.practicum.ewm_service.entity.constant.EventState;
import ru.practicum.ewm_service.repository.CommentRepository;
import ru.practicum.ewm_service.service.admin.AdminCommentService;
import ru.practicum.ewm_service.service.admin.AdminEventService;
import ru.practicum.ewm_service.service.admin.AdminUserService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class CommentServiceTest {
    private final Timestamp timestamp = Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 1, 1));
    private final Timestamp timestampEventDate = Timestamp.valueOf(LocalDateTime.of(2024, 1, 1, 1, 1));
    @Autowired
    private AdminCommentService commentService;
    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private AdminEventService eventService;
    @MockBean
    private AdminUserService userService;
    private Comment comment;
    private User user;
    private Event event;

    @BeforeEach
    void setUp() {
        comment = Comment.builder()
                .id(1L)
                .event(Event.builder().id(1L).build())
                .creator(User.builder().id(1L).build())
                .text("test")
                .createOn(timestamp)
                .status(CommentState.PENDING)
                .build();
        user = User.builder()
                .id(1L)
                .name("test")
                .email("test@test.test")
                .build();
        event = Event.builder()
                .id(1L)
                .annotation("test")
                .category(Category.builder().id(1L).build())
                .confirmedRequests(1)
                .createdOn(timestamp)
                .description("test")
                .eventDate(timestampEventDate)
                .initiator(User.builder().build())
                .location(Location.builder().build())
                .paid(true)
                .participantLimit(2)
                .publishedOn(timestamp)
                .requestModeration(true)
                .state(EventState.PENDING)
                .title("test")
                .views(1)
                .build();
    }

    @AfterEach
    void mockVerify() {
        verifyNoMoreInteractions(
                commentRepository,
                eventService,
                userService
        );
    }

    @Test
    @DisplayName("Проверяем метод createComment сервиса CommentService.")
    void createCommentMethodTest() {
        when(userService.getById(anyLong())).thenReturn(user);
        when(eventService.findEvent(anyLong(), anyString())).thenReturn(event);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        //test
        Comment test = commentService.createComment(1L, 1L, comment);
        assertEquals(1L, test.getId());
        assertNotNull(test.getEvent());
        assertNotNull(test.getCreator());
        assertEquals("test", test.getText());
        assertEquals(timestamp, test.getCreateOn());
        assertEquals(CommentState.PENDING, test.getStatus());

        verify(userService, times(1)).getById(anyLong());
        verify(eventService, times(1)).findEvent(anyLong(), anyString());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    @DisplayName("Проверяем метод updateComment сервиса CommentService.")
    void updateCommentMethodTest() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(eventService.findEvent(anyLong(), anyString())).thenReturn(event);
        Comment update = Comment.builder()
                .id(1L)
                .text("testtest")
                .build();

        //test
        Comment test = commentService.updateComment(1L, 1L, 1L, update);
        assertEquals(1L, test.getId());
        assertNotNull(test.getEvent());
        assertNotNull(test.getCreator());
        assertEquals("testtest", test.getText());
        assertEquals(timestamp, test.getCreateOn());
        assertEquals(CommentState.PENDING, test.getStatus());

        verify(eventService, times(1)).findEvent(anyLong(), anyString());
        verify(commentRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Проверяем метод deleteCommentByUser сервиса CommentService.")
    void deleteCommentByUserMethodTest() {
        //test
        commentService.deleteCommentByUser(1L, 1L, 1L);

        verify(commentRepository, times(1)).deleteByIdAndCreatorIdAndEventId(anyLong(), anyLong(), anyLong());
    }

    @Test
    @DisplayName("Проверяем метод getCommentsById сервиса CommentService.")
    void getCommentsByIdMethodTest() {
        when(commentRepository.findCommentByIdAndEventIdAndStatus(anyLong(), anyLong(), any(CommentState.class))).thenReturn(comment);

        //test
        Comment test = commentService.getCommentsById(1L, 1L);
        assertEquals(1L, test.getId());
        assertNotNull(test.getEvent());
        assertNotNull(test.getCreator());
        assertEquals("test", test.getText());
        assertEquals(timestamp, test.getCreateOn());
        assertEquals(CommentState.PENDING, test.getStatus());

        verify(commentRepository, times(1)).findCommentByIdAndEventIdAndStatus(anyLong(), anyLong(), any(CommentState.class));
    }

    @Test
    @DisplayName("Проверяем метод getComments сервиса CommentService.")
    void getCommentsMethodTest() {
        when(commentRepository.findAllByEventIdAndStatusOrderByIdDesc(anyLong(), any(CommentState.class), any(PageRequest.class))).thenReturn(List.of(comment));

        //test
        List<Comment> test = commentService.getComments(1L, 0, 10);
        assertEquals(1L, test.get(0).getId());
        assertNotNull(test.get(0).getEvent());
        assertNotNull(test.get(0).getCreator());
        assertEquals("test", test.get(0).getText());
        assertEquals(timestamp, test.get(0).getCreateOn());
        assertEquals(CommentState.PENDING, test.get(0).getStatus());

        verify(commentRepository, times(1)).findAllByEventIdAndStatusOrderByIdDesc(anyLong(), any(CommentState.class), any(PageRequest.class));
    }

    @Test
    @DisplayName("Проверяем метод canceledComment сервиса CommentService.")
    void canceledCommentMethodTest() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));

        //test
        Comment test = commentService.canceledComment(1L);
        assertEquals(1L, test.getId());
        assertNotNull(test.getEvent());
        assertNotNull(test.getCreator());
        assertEquals("test", test.getText());
        assertEquals(timestamp, test.getCreateOn());
        assertEquals(CommentState.CANCELED, test.getStatus());

        verify(commentRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Проверяем метод publishedComment сервиса CommentService.")
    void publishedCommentMethodTest() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));

        //test
        Comment test = commentService.publishedComment(1L);
        assertEquals(1L, test.getId());
        assertNotNull(test.getEvent());
        assertNotNull(test.getCreator());
        assertEquals("test", test.getText());
        assertEquals(timestamp, test.getCreateOn());
        assertEquals(CommentState.PUBLISHED, test.getStatus());

        verify(commentRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Проверяем метод deleteComment сервиса CommentService.")
    void deleteCommentMethodTest() {
        //test
        commentService.deleteComment(1L);

        verify(commentRepository, times(1)).deleteById(anyLong());
    }

}
