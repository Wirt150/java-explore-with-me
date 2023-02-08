package ru.practicum.ewm_service.web.privates;

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
import ru.practicum.ewm_service.entity.Event;
import ru.practicum.ewm_service.entity.Request;
import ru.practicum.ewm_service.entity.User;
import ru.practicum.ewm_service.entity.constant.RequestStatus;
import ru.practicum.ewm_service.entity.mapper.RequestMapper;
import ru.practicum.ewm_service.entity.model.request.RequestDto;
import ru.practicum.ewm_service.service.RequestsService;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PrivateRequestsController.class)
public class PrivateRequestsControllerTest {
    private final Timestamp timestamp = Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 1, 1));
    @MockBean
    private RequestsService requestsService;
    @MockBean
    private RequestMapper requestMapper;
    @Autowired
    private MockMvc mvc;
    private Request request;
    private RequestDto requestDto;

    @BeforeEach
    void setUp() {
        request = Request.builder()
                .id(1L)
                .created(timestamp)
                .event(Event.builder().build())
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
    }

    @AfterEach
    void mockVerify() {
        verifyNoMoreInteractions(
                requestsService,
                requestMapper
        );
    }

    @Test
    @DisplayName("Проверяем эндпоинт createRequest контроллера PrivateEventController.")
    void createRequestTest() throws Exception {
        when(requestsService.createRequest(anyLong(), anyLong())).thenReturn(request);
        when(requestMapper.toRequestDto(ArgumentMatchers.any(Request.class))).thenReturn(requestDto);

        //test
        mvc.perform(post("/users/1/requests")
                        .param("userId", "1")
                        .param("eventId", "1")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.created", is("2019-12-31 22:01:00")))
                .andExpect(jsonPath("$.event", is(requestDto.getEvent())))
                .andExpect(jsonPath("$.requester", is(requestDto.getRequester())))
                .andExpect(jsonPath("$.status", is(requestDto.getStatus().toString())));

        verify(requestsService, times(1)).createRequest(anyLong(), anyLong());
        verify(requestMapper, times(1)).toRequestDto(ArgumentMatchers.any(Request.class));
    }

    @Test
    @DisplayName("Проверяем эндпоинт cancelRequest контроллера PrivateEventController.")
    void cancelRequestTest() throws Exception {
        when(requestsService.cancelRequest(anyLong(), anyLong())).thenReturn(request);
        when(requestMapper.toRequestDto(ArgumentMatchers.any(Request.class))).thenReturn(requestDto);

        //test
        mvc.perform(patch("/users/1/requests/1/cancel")
                        .param("userId", "1")
                        .param("requestId", "1")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.created", is("2019-12-31 22:01:00")))
                .andExpect(jsonPath("$.event", is(requestDto.getEvent())))
                .andExpect(jsonPath("$.requester", is(requestDto.getRequester())))
                .andExpect(jsonPath("$.status", is(requestDto.getStatus().toString())));

        verify(requestsService, times(1)).cancelRequest(anyLong(), anyLong());
        verify(requestMapper, times(1)).toRequestDto(ArgumentMatchers.any(Request.class));
    }

    @Test
    @DisplayName("Проверяем эндпоинт getRequestByUser контроллера PrivateEventController.")
    void getRequestByUserTest() throws Exception {
        when(requestsService.getRequestByUser(anyLong())).thenReturn(List.of(request));
        when(requestMapper.toRequestDtos(ArgumentMatchers.any(List.class))).thenReturn(List.of(requestDto));

        //test
        mvc.perform(get("/users/1/requests/")
                        .param("userId", "1")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].created", is("2019-12-31 22:01:00")))
                .andExpect(jsonPath("$[0].event", is(requestDto.getEvent())))
                .andExpect(jsonPath("$[0].requester", is(requestDto.getRequester())))
                .andExpect(jsonPath("$[0].status", is(requestDto.getStatus().toString())));

        verify(requestsService, times(1)).getRequestByUser(anyLong());
        verify(requestMapper, times(1)).toRequestDtos(ArgumentMatchers.any(List.class));
    }
}
