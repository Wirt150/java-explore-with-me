package ru.practicum.ewm_service.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.ewm_service.entity.*;
import ru.practicum.ewm_service.entity.constant.EventState;
import ru.practicum.ewm_service.entity.constant.RequestStatus;
import ru.practicum.ewm_service.repository.RequestRepository;
import ru.practicum.ewm_service.service.admin.AdminEventService;
import ru.practicum.ewm_service.service.admin.AdminUserService;

import javax.transaction.Transactional;
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
public class RequestsServiceTest {
    private final Timestamp timestamp = Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 1, 1));
    private final Timestamp timestampEventDate = Timestamp.valueOf(LocalDateTime.of(2024, 1, 1, 1, 1));
    @Autowired
    private RequestsService requestsService;
    @MockBean
    private RequestRepository requestRepository;
    @MockBean
    private AdminEventService eventService;
    @MockBean
    private AdminUserService userService;
    private Request request;
    private Event event;
    private User user;

    @BeforeEach
    void setUp() {
        request = Request.builder()
                .id(1L)
                .created(timestamp)
                .event(Event.builder().id(1L).build())
                .requester(User.builder().id(1L).build())
                .status(RequestStatus.PENDING)
                .build();
        event = Event.builder()
                .id(1L)
                .annotation("test")
                .category(Category.builder().build())
                .confirmedRequests(1)
                .createdOn(timestamp)
                .description("test")
                .eventDate(timestampEventDate)
                .initiator(User.builder().id(1L).build())
                .location(Location.builder().build())
                .paid(true)
                .participantLimit(2)
                .publishedOn(timestamp)
                .requestModeration(true)
                .state(EventState.PENDING)
                .title("test")
                .views(1)
                .build();
        user = User.builder()
                .id(1L)
                .name("test")
                .email("test@test.test")
                .build();
    }

    @AfterEach
    void mockVerify() {
        verifyNoMoreInteractions(
                requestRepository,
                eventService,
                userService
        );
    }

    @Test
    @DisplayName("Проверяем метод createRequest сервиса RequestsService.")
    void createRequestMethodTest() {
        when(requestRepository.existsByEventIdAndRequesterId(anyLong(), anyLong())).thenReturn(false);
        when(eventService.findEvent(anyLong(), anyString())).thenReturn(event);
        when(userService.getById(anyLong())).thenReturn(user);
        when(requestRepository.save(any(Request.class))).thenReturn(request);

        //test
        Request test = requestsService.createRequest(1L, 1L);

        assertEquals(1L, test.getId());
        assertEquals(timestamp, test.getCreated());
        assertNotNull(test.getEvent());
        assertNotNull(test.getRequester());
        assertEquals(RequestStatus.PENDING, test.getStatus());

        verify(requestRepository, times(1)).existsByEventIdAndRequesterId(anyLong(), anyLong());
        verify(requestRepository, times(1)).countAllByEventId(anyLong());
        verify(eventService, times(1)).findEvent(anyLong(), anyString());
        verify(userService, times(1)).getById(anyLong());
        verify(requestRepository, times(1)).save(any(Request.class));
    }

    @Test
    @DisplayName("Проверяем метод cancelRequest сервиса RequestsService.")
    void cancelRequestMethodTest() {
        when(requestRepository.findRequestByIdAndRequesterId(anyLong(), anyLong())).thenReturn(Optional.of(request));
        when(requestRepository.save(any(Request.class))).thenReturn(request);

        //test
        Request test = requestsService.cancelRequest(1L, 1L);

        assertEquals(1L, test.getId());
        assertEquals(timestamp, test.getCreated());
        assertNotNull(test.getEvent());
        assertNotNull(test.getRequester());
        assertEquals(RequestStatus.CANCELED, test.getStatus());

        verify(requestRepository, times(1)).findRequestByIdAndRequesterId(anyLong(), anyLong());
        verify(requestRepository, times(1)).save(any(Request.class));
    }

    @Test
    @DisplayName("Проверяем метод getRequestByUser сервиса RequestsService.")
    void getRequestByUserMethodTest() {
        when(requestRepository.findAllByRequesterId(anyLong())).thenReturn(List.of(request));

        //test
        List<Request> test = requestsService.getRequestByUser(1L);

        assertEquals(1, test.size());
        assertEquals(1L, test.get(0).getId());
        assertEquals(timestamp, test.get(0).getCreated());
        assertNotNull(test.get(0).getEvent());
        assertNotNull(test.get(0).getRequester());
        assertEquals(RequestStatus.PENDING, test.get(0).getStatus());

        verify(requestRepository, times(1)).findAllByRequesterId(anyLong());
    }

    @Test
    @DisplayName("Проверяем метод findRequestByEventUser сервиса RequestsService.")
    void findRequestByEventUserMethodTest() {
        when(requestRepository.findAllByEventIdAndEvent_InitiatorId(anyLong(), anyLong())).thenReturn(List.of(request));

        //test
        List<Request> test = requestsService.findRequestByEventUser(1L, 1L);

        assertEquals(1, test.size());
        assertEquals(1L, test.get(0).getId());
        assertEquals(timestamp, test.get(0).getCreated());
        assertNotNull(test.get(0).getEvent());
        assertNotNull(test.get(0).getRequester());
        assertEquals(RequestStatus.PENDING, test.get(0).getStatus());

        verify(requestRepository, times(1)).findAllByEventIdAndEvent_InitiatorId(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Проверяем метод confirmRequest сервиса RequestsService.")
    void confirmRequestMethodTest() {
        when(eventService.findEvent(anyLong(), anyString())).thenReturn(event);
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(request));

        //test
        Request test = requestsService.confirmRequest(1L, 1L, 1L);

        assertEquals(1L, test.getId());
        assertEquals(timestamp, test.getCreated());
        assertNotNull(test.getEvent());
        assertNotNull(test.getRequester());
        assertEquals(RequestStatus.CONFIRMED, test.getStatus());

        verify(eventService, times(1)).findEvent(anyLong(), anyString());
        verify(requestRepository, times(1)).findById(anyLong());
        verify(requestRepository, times(1)).updateCanceledAllRequest(anyLong());
    }

    @Test
    @Transactional
    @DisplayName("Проверяем метод rejectRequest сервиса RequestsService.")
    void rejectRequestMethodTest() {
        when(eventService.findEvent(anyLong(), anyString())).thenReturn(event);
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(request));

        //test
        Request test = requestsService.rejectRequest(1L, 1L, 1L);

        assertEquals(1L, test.getId());
        assertEquals(timestamp, test.getCreated());
        assertNotNull(test.getEvent());
        assertNotNull(test.getRequester());
        assertEquals(RequestStatus.REJECTED, test.getStatus());

        verify(eventService, times(1)).findEvent(anyLong(), anyString());
        verify(requestRepository, times(1)).findById(anyLong());
    }
}
