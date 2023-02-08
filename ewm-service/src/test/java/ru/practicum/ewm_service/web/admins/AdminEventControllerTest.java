package ru.practicum.ewm_service.web.admins;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ewm_service.entity.*;
import ru.practicum.ewm_service.entity.constant.CommentState;
import ru.practicum.ewm_service.entity.constant.EventState;
import ru.practicum.ewm_service.entity.mapper.CommentMapper;
import ru.practicum.ewm_service.entity.mapper.EventMapper;
import ru.practicum.ewm_service.entity.model.comment.response.CommentResponseDto;
import ru.practicum.ewm_service.entity.model.event.request.AdminEventSearchRequest;
import ru.practicum.ewm_service.entity.model.event.request.AdminUpdateEventRequest;
import ru.practicum.ewm_service.entity.model.event.response.EventFullDto;
import ru.practicum.ewm_service.entity.model.user.UserShortDto;
import ru.practicum.ewm_service.service.admin.AdminCommentService;
import ru.practicum.ewm_service.service.admin.AdminEventService;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminEventController.class)
public class AdminEventControllerTest {
    private final Timestamp timestamp = Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 1, 1));
    private final Timestamp timestampEventDate = Timestamp.valueOf(LocalDateTime.of(2022, 1, 1, 1, 1));
    @MockBean
    private AdminEventService adminEventService;
    @MockBean
    private AdminCommentService adminCommentService;
    @MockBean
    private EventMapper eventMapper;
    @MockBean
    private CommentMapper commentMapper;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    private Event event;
    private EventFullDto eventFullDto;
    private Comment comment;
    private CommentResponseDto commentResponseDto;
    private AdminUpdateEventRequest adminUpdateEventRequest;

    @BeforeEach
    void setUp() {
        event = Event.builder()
                .id(1L)
                .annotation("test")
                .category(Category.builder().build())
                .confirmedRequests(1)
                .createdOn(timestamp)
                .description("test")
                .eventDate(timestampEventDate)
                .initiator(User.builder().build())
                .location(Location.builder().build())
                .paid(true)
                .participantLimit(1)
                .publishedOn(timestamp)
                .requestModeration(true)
                .state(EventState.PENDING)
                .title("test")
                .views(1)
                .build();
        eventFullDto = EventFullDto.builder()
                .id(1L)
                .annotation("test")
                .category(Category.builder().build())
                .confirmedRequests(1)
                .createdOn(timestamp)
                .description("test")
                .eventDate(timestampEventDate)
                .initiator(UserShortDto.builder().build())
                .location(Location.builder().build())
                .paid(true)
                .participantLimit(1)
                .publishedOn(timestamp)
                .requestModeration(true)
                .state(EventState.PENDING)
                .title("test")
                .views(1)
                .build();
        comment = Comment.builder()
                .id(1L)
                .event(event)
                .creator(User.builder().build())
                .text("test")
                .createOn(timestamp)
                .status(CommentState.PENDING)
                .build();
        commentResponseDto = CommentResponseDto.builder()
                .id(1L)
                .creator(UserShortDto.builder().build())
                .text("test")
                .createOn(timestamp)
                .status(CommentState.PENDING)
                .build();
        adminUpdateEventRequest = AdminUpdateEventRequest.builder()
                .annotation("test")
                .category(1L)
                .description("test")
                .eventDate(timestampEventDate)
                .location(Location.builder().build())
                .paid(true)
                .participantLimit(1)
                .requestModeration(true)
                .title("test")
                .build();
    }

    @AfterEach
    void mockVerify() {
        verifyNoMoreInteractions(
                adminEventService,
                adminCommentService,
                eventMapper,
                commentMapper
        );
    }

    @Test
    @DisplayName("Проверяем эндпоинт eventSearch контроллера AdminEventController.")
    void eventSearchTest() throws Exception {
        when(adminEventService.eventSearchAdmin(ArgumentMatchers.any(AdminEventSearchRequest.class))).thenReturn(List.of(event));
        when(eventMapper.toEventFullDtos(anyList())).thenReturn(List.of(eventFullDto));

        //test
        mvc.perform(get("/admin/events")
                        .param("from", "1")
                        .param("size", "1")
                        .param("users", "1")
                        .param("states", "PENDING")
                        .param("categories", "1")
                        .param("rangeStart", "1991-01-01 01:01:01")
                        .param("rangeEnd", "1991-01-02 01:01:01")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(eventFullDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].annotation", is(eventFullDto.getAnnotation())))
                .andExpect(jsonPath("$[0].category", notNullValue()))
                .andExpect(jsonPath("$[0].confirmedRequests", is(eventFullDto.getConfirmedRequests()), Integer.class))
                .andExpect(jsonPath("$[0].eventDate", is("2021-12-31 22:01:00")))
                .andExpect(jsonPath("$[0].initiator", notNullValue()))
                .andExpect(jsonPath("$[0].paid", is(true)))
                .andExpect(jsonPath("$[0].title", is(eventFullDto.getTitle())))
                .andExpect(jsonPath("$[0].views", is(eventFullDto.getViews())));

        verify(adminEventService, times(1)).eventSearchAdmin(ArgumentMatchers.any(AdminEventSearchRequest.class));
        verify(eventMapper, times(1)).toEventFullDtos(anyList());
    }

    @Test
    @DisplayName("Проверяем эндпоинт editEvent контроллера AdminEventController.")
    void editEventTest() throws Exception {
        when(adminEventService.editEvent(ArgumentMatchers.any(AdminUpdateEventRequest.class), anyLong())).thenReturn(event);
        when(eventMapper.toEventFullDto(ArgumentMatchers.any(Event.class))).thenReturn(eventFullDto);

        //test
        mvc.perform(put("/admin/events/1")
                        .param("eventId", "1")
                        .content(mapper.writeValueAsString(adminUpdateEventRequest))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(eventFullDto.getId()), Long.class))
                .andExpect(jsonPath("$.annotation", is(eventFullDto.getAnnotation())))
                .andExpect(jsonPath("$.category", notNullValue()))
                .andExpect(jsonPath("$.confirmedRequests", is(eventFullDto.getConfirmedRequests()), Integer.class))
                .andExpect(jsonPath("$.createdOn", is("2019-12-31 22:01:00")))
                .andExpect(jsonPath("$.description", is(eventFullDto.getDescription())))
                .andExpect(jsonPath("$.eventDate", is("2021-12-31 22:01:00")))
                .andExpect(jsonPath("$.createdOn", is("2019-12-31 22:01:00")))
                .andExpect(jsonPath("$.initiator", notNullValue()))
                .andExpect(jsonPath("$.location", notNullValue()))
                .andExpect(jsonPath("$.paid", is(true)))
                .andExpect(jsonPath("$.participantLimit", is(eventFullDto.getParticipantLimit()), Integer.class))
                .andExpect(jsonPath("$.publishedOn", is("2019-12-31 22:01:00")))
                .andExpect(jsonPath("$.requestModeration", is(true)))
                .andExpect(jsonPath("$.state", is(eventFullDto.getState().toString())))
                .andExpect(jsonPath("$.title", is(eventFullDto.getTitle())))
                .andExpect(jsonPath("$.views", is(eventFullDto.getViews())));

        verify(adminEventService, times(1)).editEvent(ArgumentMatchers.any(AdminUpdateEventRequest.class), anyLong());
        verify(eventMapper, times(1)).toEventFullDto(ArgumentMatchers.any(Event.class));
    }

    @Test
    @DisplayName("Проверяем эндпоинт publishEvent контроллера AdminEventController.")
    void publishEventTest() throws Exception {
        when(adminEventService.publishEvent(anyLong())).thenReturn(event);
        when(eventMapper.toEventFullDto(ArgumentMatchers.any(Event.class))).thenReturn(eventFullDto);

        //test
        mvc.perform(patch("/admin/events/1/publish")
                        .param("eventId", "1")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(eventFullDto.getId()), Long.class))
                .andExpect(jsonPath("$.annotation", is(eventFullDto.getAnnotation())))
                .andExpect(jsonPath("$.category", notNullValue()))
                .andExpect(jsonPath("$.confirmedRequests", is(eventFullDto.getConfirmedRequests()), Integer.class))
                .andExpect(jsonPath("$.createdOn", is("2019-12-31 22:01:00")))
                .andExpect(jsonPath("$.description", is(eventFullDto.getDescription())))
                .andExpect(jsonPath("$.eventDate", is("2021-12-31 22:01:00")))
                .andExpect(jsonPath("$.createdOn", is("2019-12-31 22:01:00")))
                .andExpect(jsonPath("$.initiator", notNullValue()))
                .andExpect(jsonPath("$.location", notNullValue()))
                .andExpect(jsonPath("$.paid", is(true)))
                .andExpect(jsonPath("$.participantLimit", is(eventFullDto.getParticipantLimit()), Integer.class))
                .andExpect(jsonPath("$.publishedOn", is("2019-12-31 22:01:00")))
                .andExpect(jsonPath("$.requestModeration", is(true)))
                .andExpect(jsonPath("$.state", is(eventFullDto.getState().toString())))
                .andExpect(jsonPath("$.title", is(eventFullDto.getTitle())))
                .andExpect(jsonPath("$.views", is(eventFullDto.getViews())));

        verify(adminEventService, times(1)).publishEvent(anyLong());
        verify(eventMapper, times(1)).toEventFullDto(ArgumentMatchers.any(Event.class));
    }

    @Test
    @DisplayName("Проверяем эндпоинт rejectEvent контроллера AdminEventController.")
    void rejectEventTest() throws Exception {
        when(adminEventService.rejectEvent(anyLong())).thenReturn(event);
        when(eventMapper.toEventFullDto(ArgumentMatchers.any(Event.class))).thenReturn(eventFullDto);

        //test
        mvc.perform(patch("/admin/events/1/reject")
                        .param("eventId", "1")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(eventFullDto.getId()), Long.class))
                .andExpect(jsonPath("$.annotation", is(eventFullDto.getAnnotation())))
                .andExpect(jsonPath("$.category", notNullValue()))
                .andExpect(jsonPath("$.confirmedRequests", is(eventFullDto.getConfirmedRequests()), Integer.class))
                .andExpect(jsonPath("$.createdOn", is("2019-12-31 22:01:00")))
                .andExpect(jsonPath("$.description", is(eventFullDto.getDescription())))
                .andExpect(jsonPath("$.eventDate", is("2021-12-31 22:01:00")))
                .andExpect(jsonPath("$.createdOn", is("2019-12-31 22:01:00")))
                .andExpect(jsonPath("$.initiator", notNullValue()))
                .andExpect(jsonPath("$.location", notNullValue()))
                .andExpect(jsonPath("$.paid", is(true)))
                .andExpect(jsonPath("$.participantLimit", is(eventFullDto.getParticipantLimit()), Integer.class))
                .andExpect(jsonPath("$.publishedOn", is("2019-12-31 22:01:00")))
                .andExpect(jsonPath("$.requestModeration", is(true)))
                .andExpect(jsonPath("$.state", is(eventFullDto.getState().toString())))
                .andExpect(jsonPath("$.title", is(eventFullDto.getTitle())))
                .andExpect(jsonPath("$.views", is(eventFullDto.getViews())));

        verify(adminEventService, times(1)).rejectEvent(anyLong());
        verify(eventMapper, times(1)).toEventFullDto(ArgumentMatchers.any(Event.class));
    }

    @Test
    @DisplayName("Проверяем эндпоинт canceledComment контроллера AdminEventController.")
    void canceledCommentTest() throws Exception {
        when(adminCommentService.canceledComment(anyLong())).thenReturn(comment);
        when(commentMapper.toCommentResponseDto(ArgumentMatchers.any(Comment.class))).thenReturn(commentResponseDto);

        //test
        mvc.perform(patch("/admin/events/comment/1/canceled")
                        .param("commentId", "1")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(eventFullDto.getId()), Long.class))
                .andExpect(jsonPath("$.creator", notNullValue()))
                .andExpect(jsonPath("$.text", is("test")))
                .andExpect(jsonPath("$.createOn", is("2019-12-31 22:01:00")))
                .andExpect(jsonPath("$.status", is(comment.getStatus().toString())));

        verify(adminCommentService, times(1)).canceledComment(anyLong());
        verify(commentMapper, times(1)).toCommentResponseDto(ArgumentMatchers.any(Comment.class));
    }

    @Test
    @DisplayName("Проверяем эндпоинт publishedComment контроллера AdminEventController.")
    void publishedCommentTest() throws Exception {
        when(adminCommentService.publishedComment(anyLong())).thenReturn(comment);
        when(commentMapper.toCommentResponseDto(ArgumentMatchers.any(Comment.class))).thenReturn(commentResponseDto);

        //test
        mvc.perform(patch("/admin/events/comment/1/published")
                        .param("commentId", "1")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(eventFullDto.getId()), Long.class))
                .andExpect(jsonPath("$.creator", notNullValue()))
                .andExpect(jsonPath("$.text", is("test")))
                .andExpect(jsonPath("$.createOn", is("2019-12-31 22:01:00")))
                .andExpect(jsonPath("$.status", is(comment.getStatus().toString())));

        verify(adminCommentService, times(1)).publishedComment(anyLong());
        verify(commentMapper, times(1)).toCommentResponseDto(ArgumentMatchers.any(Comment.class));
    }

    @Test
    @DisplayName("Проверяем эндпоинт deleteComment контроллера AdminEventController.")
    void deleteCommentTest() throws Exception {
        //test
        mvc.perform(delete("/admin/events/comment/1")
                        .param("commentId", "1")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());

        verify(adminCommentService, times(1)).deleteComment(anyLong());
    }
}
