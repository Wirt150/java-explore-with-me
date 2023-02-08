package ru.practicum.ewm_service.web.privates;

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
import ru.practicum.ewm_service.entity.constant.RequestStatus;
import ru.practicum.ewm_service.entity.mapper.CommentMapper;
import ru.practicum.ewm_service.entity.mapper.EventMapper;
import ru.practicum.ewm_service.entity.mapper.RequestMapper;
import ru.practicum.ewm_service.entity.model.comment.request.CommentRequestDto;
import ru.practicum.ewm_service.entity.model.comment.response.CommentResponseDto;
import ru.practicum.ewm_service.entity.model.event.request.EventNewDto;
import ru.practicum.ewm_service.entity.model.event.request.EventUpdateRequest;
import ru.practicum.ewm_service.entity.model.event.response.EventFullDto;
import ru.practicum.ewm_service.entity.model.event.response.EventShortDto;
import ru.practicum.ewm_service.entity.model.request.RequestDto;
import ru.practicum.ewm_service.entity.model.user.UserShortDto;
import ru.practicum.ewm_service.service.CommentService;
import ru.practicum.ewm_service.service.EventService;
import ru.practicum.ewm_service.service.RequestsService;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PrivateEventController.class)
public class PrivateEventControllerTest {
    private final Timestamp timestamp = Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 1, 1));
    private final Timestamp timestampEventDate = Timestamp.valueOf(LocalDateTime.of(2024, 1, 1, 1, 1));
    @MockBean
    private EventService eventService;
    @MockBean
    private RequestsService requestsService;
    @MockBean
    private CommentService commentsService;
    @MockBean
    private EventMapper eventMapper;
    @MockBean
    private RequestMapper requestMapper;
    @MockBean
    private CommentMapper commentMapper;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    private Event event;
    private EventNewDto eventNewDto;
    private EventUpdateRequest eventUpdateRequest;
    private EventFullDto eventFullDto;
    private EventShortDto eventShortDto;
    private Request request;
    private RequestDto requestDto;
    private Comment comment;
    private CommentRequestDto commentRequestDto;
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
        eventNewDto = EventNewDto.builder()
                .annotation("testtesttesttesttesttesttest")
                .category(1L)
                .description("testtesttesttesttesttesttest")
                .eventDate(timestampEventDate)
                .location(Location.builder().build())
                .paid(true)
                .participantLimit(1)
                .requestModeration(true)
                .title("test")
                .build();
        eventUpdateRequest = EventUpdateRequest.builder()
                .eventId(1L)
                .annotation("testtesttesttesttesttesttest")
                .category(1L)
                .description("testtesttesttesttesttesttest")
                .eventDate(timestampEventDate)
                .paid(true)
                .participantLimit(1)
                .title("test")
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
        request = Request.builder()
                .id(1L)
                .created(timestamp)
                .event(event)
                .requester(User.builder().build())
                .status(RequestStatus.PENDING)
                .build();
        requestDto = RequestDto.builder()
                .id(1L)
                .created(timestamp)
                .event(1)
                .requester(1)
                .status(RequestStatus.PENDING)
                .build();
        comment = Comment.builder()
                .id(1L)
                .event(event)
                .creator(User.builder().build())
                .text("test")
                .createOn(timestamp)
                .status(CommentState.PENDING)
                .build();
        commentRequestDto = CommentRequestDto.builder()
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
                eventService,
                requestsService,
                commentsService,
                eventMapper,
                requestMapper,
                commentMapper
        );
    }

    @Test
    @DisplayName("Проверяем эндпоинт createEvent контроллера PrivateEventController.")
    void createEventTest() throws Exception {
        when(eventService.createEvent(ArgumentMatchers.any(Event.class), anyLong())).thenReturn(event);
        when(eventMapper.toEvent(ArgumentMatchers.any(EventNewDto.class))).thenReturn(event);
        when(eventMapper.toEventFullDto(ArgumentMatchers.any(Event.class))).thenReturn(eventFullDto);

        //test
        mvc.perform(post("/users/1/events")
                        .param("userId", "1")
                        .content(mapper.writeValueAsString(eventNewDto))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(eventFullDto.getId()), Long.class))
                .andExpect(jsonPath("$.annotation", is(eventFullDto.getAnnotation())))
                .andExpect(jsonPath("$.category", notNullValue()))
                .andExpect(jsonPath("$.confirmedRequests", is(eventFullDto.getConfirmedRequests()), Integer.class))
                .andExpect(jsonPath("$.createdOn", is("2019-12-31 22:01:00")))
                .andExpect(jsonPath("$.description", is(eventFullDto.getDescription())))
                .andExpect(jsonPath("$.eventDate", is("2023-12-31 22:01:00")))
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

        verify(eventService, times(1)).createEvent(ArgumentMatchers.any(Event.class), anyLong());
        verify(eventMapper, times(1)).toEvent(ArgumentMatchers.any(EventNewDto.class));
        verify(eventMapper, times(1)).toEventFullDto(ArgumentMatchers.any(Event.class));
    }

    @Test
    @DisplayName("Проверяем эндпоинт patchEvent контроллера PrivateEventController.")
    void patchEventTest() throws Exception {
        when(eventService.updateEvent(ArgumentMatchers.any(EventUpdateRequest.class), anyLong())).thenReturn(event);
        when(eventMapper.toEventFullDto(ArgumentMatchers.any(Event.class))).thenReturn(eventFullDto);

        //test
        mvc.perform(patch("/users/1/events")
                        .param("userId", "1")
                        .content(mapper.writeValueAsString(eventUpdateRequest))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(eventFullDto.getId()), Long.class))
                .andExpect(jsonPath("$.annotation", is(eventFullDto.getAnnotation())))
                .andExpect(jsonPath("$.category", notNullValue()))
                .andExpect(jsonPath("$.confirmedRequests", is(eventFullDto.getConfirmedRequests()), Integer.class))
                .andExpect(jsonPath("$.createdOn", is("2019-12-31 22:01:00")))
                .andExpect(jsonPath("$.description", is(eventFullDto.getDescription())))
                .andExpect(jsonPath("$.eventDate", is("2023-12-31 22:01:00")))
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

        verify(eventService, times(1)).updateEvent(ArgumentMatchers.any(EventUpdateRequest.class), anyLong());
        verify(eventMapper, times(1)).toEventFullDto(ArgumentMatchers.any(Event.class));
    }

    @Test
    @DisplayName("Проверяем эндпоинт cancelEvent контроллера PrivateEventController.")
    void cancelEventTest() throws Exception {
        when(eventService.cancelEvent(anyLong(), anyLong())).thenReturn(event);
        when(eventMapper.toEventFullDto(ArgumentMatchers.any(Event.class))).thenReturn(eventFullDto);

        //test
        mvc.perform(patch("/users/1/events/1")
                        .param("userId", "1")
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
                .andExpect(jsonPath("$.eventDate", is("2023-12-31 22:01:00")))
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

        verify(eventService, times(1)).cancelEvent(anyLong(), anyLong());
        verify(eventMapper, times(1)).toEventFullDto(ArgumentMatchers.any(Event.class));
    }

    @Test
    @DisplayName("Проверяем эндпоинт findAllEventByUser контроллера PrivateEventController.")
    void findAllEventByUserTest() throws Exception {
        when(eventService.findAllEventByUserPage(anyLong(), anyInt(), anyInt())).thenReturn(List.of(event));
        when(eventMapper.toEventShortDtos(ArgumentMatchers.any(List.class))).thenReturn(List.of(eventShortDto));

        //test
        mvc.perform(get("/users/1/events")
                        .param("from", "0")
                        .param("size", "10")
                        .param("userId", "1")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(eventFullDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].annotation", is(eventFullDto.getAnnotation())))
                .andExpect(jsonPath("$[0].category", notNullValue()))
                .andExpect(jsonPath("$[0].confirmedRequests", is(eventFullDto.getConfirmedRequests()), Integer.class))
                .andExpect(jsonPath("$[0].eventDate", is("2023-12-31 22:01:00")))
                .andExpect(jsonPath("$[0].initiator", notNullValue()))
                .andExpect(jsonPath("$[0].paid", is(true)))
                .andExpect(jsonPath("$[0].title", is(eventFullDto.getTitle())))
                .andExpect(jsonPath("$[0].views", is(eventFullDto.getViews())));

        verify(eventService, times(1)).findAllEventByUserPage(anyLong(), anyInt(), anyInt());
        verify(eventMapper, times(1)).toEventShortDtos(ArgumentMatchers.any(List.class));
    }

    @Test
    @DisplayName("Проверяем эндпоинт findEventByUser контроллера PrivateEventController.")
    void findEventByUserTest() throws Exception {
        when(eventService.findEventByUser(anyLong(), anyLong())).thenReturn(event);
        when(eventMapper.toEventFullDto(ArgumentMatchers.any(Event.class))).thenReturn(eventFullDto);

        //test
        mvc.perform(get("/users/1/events/1")
                        .param("userId", "1")
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
                .andExpect(jsonPath("$.eventDate", is("2023-12-31 22:01:00")))
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

        verify(eventService, times(1)).findEventByUser(anyLong(), anyLong());
        verify(eventMapper, times(1)).toEventFullDto(ArgumentMatchers.any(Event.class));
    }

    @Test
    @DisplayName("Проверяем эндпоинт findRequestByEventUser контроллера PrivateEventController.")
    void findRequestByEventUserTest() throws Exception {
        when(requestsService.findRequestByEventUser(anyLong(), anyLong())).thenReturn(List.of(request));
        when(requestMapper.toRequestDtos(ArgumentMatchers.any(List.class))).thenReturn(List.of(requestDto));

        //test
        mvc.perform(get("/users/1/events/1/requests")
                        .param("userId", "1")
                        .param("eventId", "1")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(eventFullDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].created", is("2019-12-31 22:01:00")))
                .andExpect(jsonPath("$[0].event", is(requestDto.getEvent())))
                .andExpect(jsonPath("$[0].requester", is(requestDto.getRequester())))
                .andExpect(jsonPath("$[0].status", is(requestDto.getStatus().toString())));

        verify(requestsService, times(1)).findRequestByEventUser(anyLong(), anyLong());
        verify(requestMapper, times(1)).toRequestDtos(ArgumentMatchers.any(List.class));
    }

    @Test
    @DisplayName("Проверяем эндпоинт confirmRequest контроллера PrivateEventController.")
    void confirmRequestTest() throws Exception {
        when(requestsService.confirmRequest(anyLong(), anyLong(), anyLong())).thenReturn(request);
        when(requestMapper.toRequestDto(ArgumentMatchers.any(Request.class))).thenReturn(requestDto);

        //test
        mvc.perform(patch("/users/1/events/1/requests/1/confirm")
                        .param("userId", "1")
                        .param("eventId", "1")
                        .param("reqId", "1")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.created", is("2019-12-31 22:01:00")))
                .andExpect(jsonPath("$.event", is(requestDto.getEvent())))
                .andExpect(jsonPath("$.requester", is(requestDto.getRequester())))
                .andExpect(jsonPath("$.status", is(requestDto.getStatus().toString())));

        verify(requestsService, times(1)).confirmRequest(anyLong(), anyLong(), anyLong());
        verify(requestMapper, times(1)).toRequestDto(ArgumentMatchers.any(Request.class));
    }

    @Test
    @DisplayName("Проверяем эндпоинт rejectRequest контроллера PrivateEventController.")
    void rejectRequestTest() throws Exception {
        when(requestsService.rejectRequest(anyLong(), anyLong(), anyLong())).thenReturn(request);
        when(requestMapper.toRequestDto(ArgumentMatchers.any(Request.class))).thenReturn(requestDto);

        //test
        mvc.perform(patch("/users/1/events/1/requests/1/reject")
                        .param("userId", "1")
                        .param("eventId", "1")
                        .param("reqId", "1")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.created", is("2019-12-31 22:01:00")))
                .andExpect(jsonPath("$.event", is(requestDto.getEvent())))
                .andExpect(jsonPath("$.requester", is(requestDto.getRequester())))
                .andExpect(jsonPath("$.status", is(requestDto.getStatus().toString())));

        verify(requestsService, times(1)).rejectRequest(anyLong(), anyLong(), anyLong());
        verify(requestMapper, times(1)).toRequestDto(ArgumentMatchers.any(Request.class));
    }

    @Test
    @DisplayName("Проверяем эндпоинт createComment контроллера PrivateEventController.")
    void createCommentTest() throws Exception {
        when(commentsService.createComment(anyLong(), anyLong(), ArgumentMatchers.any(Comment.class))).thenReturn(comment);
        when(commentMapper.toComment(ArgumentMatchers.any(CommentRequestDto.class))).thenReturn(comment);
        when(commentMapper.toCommentResponseDto(ArgumentMatchers.any(Comment.class))).thenReturn(commentResponseDto);

        //test
        mvc.perform(post("/users/1/events/1/comment")
                        .param("userId", "1")
                        .param("eventId", "1")
                        .content(mapper.writeValueAsString(commentRequestDto))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.creator", notNullValue()))
                .andExpect(jsonPath("$.text", is("test")))
                .andExpect(jsonPath("$.createOn", is("2019-12-31 22:01:00")))
                .andExpect(jsonPath("$.status", is(comment.getStatus().toString())));

        verify(commentsService, times(1)).createComment(anyLong(), anyLong(), ArgumentMatchers.any(Comment.class));
        verify(commentMapper, times(1)).toComment(ArgumentMatchers.any(CommentRequestDto.class));
        verify(commentMapper, times(1)).toCommentResponseDto(ArgumentMatchers.any(Comment.class));
    }

    @Test
    @DisplayName("Проверяем эндпоинт patchComment контроллера PrivateEventController.")
    void patchCommentTest() throws Exception {
        when(commentsService.updateComment(anyLong(), anyLong(), anyLong(), ArgumentMatchers.any(Comment.class))).thenReturn(comment);
        when(commentMapper.toComment(ArgumentMatchers.any(CommentRequestDto.class))).thenReturn(comment);
        when(commentMapper.toCommentResponseDto(ArgumentMatchers.any(Comment.class))).thenReturn(commentResponseDto);

        //test
        mvc.perform(patch("/users/1/events/1/comment/1")
                        .param("userId", "1")
                        .param("eventId", "1")
                        .param("commentId", "1")
                        .content(mapper.writeValueAsString(commentRequestDto))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.creator", notNullValue()))
                .andExpect(jsonPath("$.text", is("test")))
                .andExpect(jsonPath("$.createOn", is("2019-12-31 22:01:00")))
                .andExpect(jsonPath("$.status", is(comment.getStatus().toString())));

        verify(commentsService, times(1)).updateComment(anyLong(), anyLong(), anyLong(), ArgumentMatchers.any(Comment.class));
        verify(commentMapper, times(1)).toComment(ArgumentMatchers.any(CommentRequestDto.class));
        verify(commentMapper, times(1)).toCommentResponseDto(ArgumentMatchers.any(Comment.class));
    }

    @Test
    @DisplayName("Проверяем эндпоинт deleteComment контроллера PrivateEventController.")
    void deleteCommentTest() throws Exception {
        //test
        mvc.perform(delete("/users/1/events/1/comment/1")
                        .param("userId", "1")
                        .param("eventId", "1")
                        .param("commentId", "1")
                        .content(mapper.writeValueAsString(commentRequestDto))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());

        verify(commentsService, times(1)).deleteCommentByUser(anyLong(), anyLong(), anyLong());
    }
}
