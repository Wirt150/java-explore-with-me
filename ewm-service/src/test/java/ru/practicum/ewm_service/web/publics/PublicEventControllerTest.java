package ru.practicum.ewm_service.web.publics;

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
import ru.practicum.ewm_service.entity.model.event.request.PublicEventSearchRequest;
import ru.practicum.ewm_service.entity.model.event.response.EventFullDto;
import ru.practicum.ewm_service.entity.model.event.response.EventShortDto;
import ru.practicum.ewm_service.entity.model.user.UserShortDto;
import ru.practicum.ewm_service.service.CommentService;
import ru.practicum.ewm_service.service.EventService;
import ru.practicum.ewm_service.web.StatsServerClient;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PublicEventController.class)
public class PublicEventControllerTest {
    private final Timestamp timestamp = Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 1, 1));
    private final Timestamp timestampEventDate = Timestamp.valueOf(LocalDateTime.of(2022, 1, 1, 1, 1));

    @MockBean
    private StatsServerClient statsServerClient;
    @MockBean
    private EventService eventService;
    @MockBean
    private CommentService commentService;
    @MockBean
    private EventMapper eventMapper;
    @MockBean
    private CommentMapper commentMapper;
    @Autowired
    private MockMvc mvc;
    private Event event;
    private EventFullDto eventFullDto;
    private EventShortDto eventShortDto;
    private Comment comment;
    private CommentResponseDto commentResponseDto;

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
        eventShortDto = EventShortDto.builder()
                .id(1L)
                .annotation("test")
                .category(Category.builder().build())
                .confirmedRequests(1)
                .eventDate(timestampEventDate)
                .initiator(UserShortDto.builder().build())
                .paid(true)
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
    }

    @AfterEach
    void mockVerify() {
        verifyNoMoreInteractions(
                statsServerClient,
                eventService,
                commentService,
                eventMapper,
                commentMapper
        );
    }

    @Test
    @DisplayName("Проверяем эндпоинт getEventId контроллера PublicEventControllerTest.")
    void getEventIdTest() throws Exception {
        when(eventService.findEvent(anyLong(), anyString())).thenReturn(event);
        when(eventMapper.toEventFullDto(ArgumentMatchers.any(Event.class))).thenReturn(eventFullDto);

        //test
        mvc.perform(get("/events/1")
                        .param("from", "1")
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

        verify(statsServerClient, times(1)).createHit(ArgumentMatchers.any(HttpServletRequest.class));
        verify(eventService, times(1)).findEvent(anyLong(), anyString());
        verify(eventMapper, times(1)).toEventFullDto(ArgumentMatchers.any(Event.class));
    }

    @Test
    @DisplayName("Проверяем эндпоинт eventSearch контроллера PublicEventControllerTest.")
    void eventSearchTest() throws Exception {
        when(eventService.eventSearchPublic(ArgumentMatchers.any(PublicEventSearchRequest.class))).thenReturn(List.of(event));
        when(eventMapper.toEventShortDtos(anyList())).thenReturn(List.of(eventShortDto));

        //test
        mvc.perform(get("/events/")
                        .param("from", "1")
                        .param("size", "1")
                        .param("text", "test")
                        .param("categories", "1")
                        .param("paid", "true")
                        .param("onlyAvailable", "true")
                        .param("sort", "EVENT_DATE")
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

        verify(statsServerClient, times(1)).createHit(ArgumentMatchers.any(HttpServletRequest.class));
        verify(eventService, times(1)).eventSearchPublic(ArgumentMatchers.any(PublicEventSearchRequest.class));
        verify(eventMapper, times(1)).toEventShortDtos(anyList());
    }

    @Test
    @DisplayName("Проверяем эндпоинт getComment контроллера PublicEventControllerTest.")
    void getCommentTest() throws Exception {
        when(commentService.getCommentsById(anyLong(), anyLong())).thenReturn(comment);
        when(commentMapper.toCommentResponseDto(ArgumentMatchers.any(Comment.class))).thenReturn(commentResponseDto);

        //test
        mvc.perform(get("/events/1/comment/1")
                        .param("eventId", "1")
                        .param("commentId", "1")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(eventFullDto.getId()), Long.class))
                .andExpect(jsonPath("$.creator", notNullValue()))
                .andExpect(jsonPath("$.text", is("test")))
                .andExpect(jsonPath("$.createOn", is("2019-12-31 22:01:00")))
                .andExpect(jsonPath("$.status", is(comment.getStatus().toString())));

        verify(statsServerClient, times(1)).createHit(ArgumentMatchers.any(HttpServletRequest.class));
        verify(commentService, times(1)).getCommentsById(anyLong(), anyLong());
        verify(commentMapper, times(1)).toCommentResponseDto(ArgumentMatchers.any(Comment.class));
    }

    @Test
    @DisplayName("Проверяем эндпоинт getComments контроллера PublicEventControllerTest.")
    void getCommentsTest() throws Exception {
        when(commentService.getComments(anyLong(), anyInt(), anyInt())).thenReturn(List.of(comment));
        when(commentMapper.toCommentResponseDtos(anyList())).thenReturn(List.of(commentResponseDto));

        //test
        mvc.perform(get("/events/1/comment")
                        .param("eventId", "1")
                        .param("from", "1")
                        .param("size", "1")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(eventFullDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].creator", notNullValue()))
                .andExpect(jsonPath("$[0].text", is("test")))
                .andExpect(jsonPath("$[0].createOn", is("2019-12-31 22:01:00")))
                .andExpect(jsonPath("$[0].status", is(comment.getStatus().toString())));

        verify(statsServerClient, times(1)).createHit(ArgumentMatchers.any(HttpServletRequest.class));
        verify(commentService, times(1)).getComments(anyLong(), anyInt(), anyInt());
        verify(commentMapper, times(1)).toCommentResponseDtos(anyList());
    }
}
