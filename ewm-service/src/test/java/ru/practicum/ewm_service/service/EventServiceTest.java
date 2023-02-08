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
import ru.practicum.ewm_service.entity.Category;
import ru.practicum.ewm_service.entity.Event;
import ru.practicum.ewm_service.entity.Location;
import ru.practicum.ewm_service.entity.User;
import ru.practicum.ewm_service.entity.constant.EventState;
import ru.practicum.ewm_service.entity.model.event.request.AdminEventSearchRequest;
import ru.practicum.ewm_service.entity.model.event.request.AdminUpdateEventRequest;
import ru.practicum.ewm_service.entity.model.event.request.EventUpdateRequest;
import ru.practicum.ewm_service.entity.model.event.request.PublicEventSearchRequest;
import ru.practicum.ewm_service.repository.EventRepository;
import ru.practicum.ewm_service.service.admin.AdminCategoryService;
import ru.practicum.ewm_service.service.admin.AdminEventService;
import ru.practicum.ewm_service.service.admin.AdminUserService;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class EventServiceTest {
    private final Timestamp timestamp = Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 1, 1));
    private final Timestamp timestampEventDate = Timestamp.valueOf(LocalDateTime.of(2024, 1, 1, 1, 1));
    @Autowired
    private AdminEventService eventService;
    @MockBean
    private EventRepository eventRepository;
    @MockBean
    private AdminUserService userService;
    @MockBean
    private AdminCategoryService categoryService;
    @MockBean
    private LocationService locationService;
    private Event event;
    private User user;
    private Category category;
    private Location location;

    @BeforeEach
    void setUp() {
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
        user = User.builder()
                .id(1L)
                .name("test")
                .email("test@test.test")
                .build();
        category = Category.builder()
                .id(1L)
                .name("test")
                .build();
        location = Location.builder()
                .id(1L)
                .lon(1)
                .lat(1)
                .build();
    }

    @AfterEach
    void mockVerify() {
        verifyNoMoreInteractions(
                eventRepository,
                locationService,
                userService,
                categoryService
        );
    }

    @Test
    @DisplayName("Проверяем метод createEvent сервиса EventService.")
    void createEventMethodTest() {
        when(categoryService.getCategoryById(anyLong())).thenReturn(category);
        when(userService.getById(anyLong())).thenReturn(user);
        when(locationService.create(any(Location.class))).thenReturn(location);
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        //test
        Event test = eventService.createEvent(event, 1L);

        assertEquals(1L, test.getId());
        assertEquals("test", test.getAnnotation());
        assertNotNull(test.getCategory());
        assertEquals(1, test.getConfirmedRequests());
        assertEquals(timestamp, test.getCreatedOn());
        assertEquals("test", test.getDescription());
        assertEquals(timestampEventDate, test.getEventDate());
        assertNotNull(test.getInitiator());
        assertNotNull(test.getLocation());
        assertTrue(test.isPaid());
        assertEquals(2, test.getParticipantLimit());
        assertEquals(timestamp, test.getPublishedOn());
        assertTrue(test.isRequestModeration());
        assertEquals(EventState.PENDING, test.getState());
        assertEquals("test", test.getTitle());
        assertEquals(1, test.getViews());

        verify(categoryService, times(1)).getCategoryById(anyLong());
        verify(userService, times(1)).getById(anyLong());
        verify(locationService, times(1)).create(any(Location.class));
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    @DisplayName("Проверяем метод updateEvent сервиса EventService.")
    void updateEventMethodTest() {
        when(eventRepository.findByIdAndInitiatorIdAndStateNot(anyLong(), anyLong(), any(EventState.class))).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        EventUpdateRequest eventUpdateRequest = EventUpdateRequest.builder()
                .eventId(1L)
                .annotation("testtest")
                .paid(true)
                .build();

        //test
        Event test = eventService.updateEvent(eventUpdateRequest, 1L);

        assertEquals(1L, test.getId());
        assertEquals("testtest", test.getAnnotation());
        assertNotNull(test.getCategory());
        assertEquals(1, test.getConfirmedRequests());
        assertEquals(timestamp, test.getCreatedOn());
        assertEquals("test", test.getDescription());
        assertEquals(timestampEventDate, test.getEventDate());
        assertNotNull(test.getInitiator());
        assertNotNull(test.getLocation());
        assertTrue(test.isPaid());
        assertEquals(2, test.getParticipantLimit());
        assertEquals(timestamp, test.getPublishedOn());
        assertTrue(test.isRequestModeration());
        assertEquals(EventState.PENDING, test.getState());
        assertEquals("test", test.getTitle());
        assertEquals(1, test.getViews());

        verify(eventRepository, times(1)).findByIdAndInitiatorIdAndStateNot(anyLong(), anyLong(), any(EventState.class));
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    @DisplayName("Проверяем метод findEvent сервиса EventService.")
    void findEventMethodTest() {
        when(eventRepository.findEventByIdAndState(anyLong(), any(EventState.class))).thenReturn(Optional.of(event));
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));

        //test
        Event test = eventService.findEvent(1L, "PublicEventController");

        assertEquals(1L, test.getId());
        assertEquals("test", test.getAnnotation());
        assertNotNull(test.getCategory());
        assertEquals(1, test.getConfirmedRequests());
        assertEquals(timestamp, test.getCreatedOn());
        assertEquals("test", test.getDescription());
        assertEquals(timestampEventDate, test.getEventDate());
        assertNotNull(test.getInitiator());
        assertNotNull(test.getLocation());
        assertTrue(test.isPaid());
        assertEquals(2, test.getParticipantLimit());
        assertEquals(timestamp, test.getPublishedOn());
        assertTrue(test.isRequestModeration());
        assertEquals(EventState.PENDING, test.getState());
        assertEquals("test", test.getTitle());
        assertEquals(2, test.getViews());

        //test
        Event testTest = eventService.findEvent(1L, "none");

        assertEquals(1L, testTest.getId());
        assertEquals("test", testTest.getAnnotation());
        assertNotNull(testTest.getCategory());
        assertEquals(1, testTest.getConfirmedRequests());
        assertEquals(timestamp, testTest.getCreatedOn());
        assertEquals("test", testTest.getDescription());
        assertEquals(timestampEventDate, testTest.getEventDate());
        assertNotNull(testTest.getInitiator());
        assertNotNull(testTest.getLocation());
        assertTrue(testTest.isPaid());
        assertEquals(2, testTest.getParticipantLimit());
        assertEquals(timestamp, testTest.getPublishedOn());
        assertTrue(testTest.isRequestModeration());
        assertEquals(EventState.PENDING, testTest.getState());
        assertEquals("test", testTest.getTitle());
        assertEquals(2, testTest.getViews());

        verify(eventRepository, times(1)).findEventByIdAndState(anyLong(), any(EventState.class));
        verify(eventRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Проверяем метод findEvents сервиса EventService.")
    void findEventsMethodTest() {
        when(eventRepository.findAllById(anyCollection())).thenReturn(List.of(event));

        //test
        List<Event> test = eventService.findEvents(List.of(1L));

        assertEquals(1, test.size());
        assertEquals(1L, test.get(0).getId());
        assertEquals("test", test.get(0).getAnnotation());
        assertNotNull(test.get(0).getCategory());
        assertEquals(1, test.get(0).getConfirmedRequests());
        assertEquals(timestamp, test.get(0).getCreatedOn());
        assertEquals("test", test.get(0).getDescription());
        assertEquals(timestampEventDate, test.get(0).getEventDate());
        assertNotNull(test.get(0).getInitiator());
        assertNotNull(test.get(0).getLocation());
        assertTrue(test.get(0).isPaid());
        assertEquals(2, test.get(0).getParticipantLimit());
        assertEquals(timestamp, test.get(0).getPublishedOn());
        assertTrue(test.get(0).isRequestModeration());
        assertEquals(EventState.PENDING, test.get(0).getState());
        assertEquals("test", test.get(0).getTitle());
        assertEquals(1, test.get(0).getViews());

        verify(eventRepository, times(1)).findAllById(anyCollection());
    }

    @Test
    @DisplayName("Проверяем метод findAllEventByUserPage сервиса EventService.")
    void findAllEventByUserPageMethodTest() {
        when(eventRepository.findAllEventByInitiatorId(anyLong(), any(PageRequest.class))).thenReturn(List.of(event));

        //test
        List<Event> test = eventService.findAllEventByUserPage(1L, 0, 10);

        assertEquals(1, test.size());
        assertEquals(1L, test.get(0).getId());
        assertEquals("test", test.get(0).getAnnotation());
        assertNotNull(test.get(0).getCategory());
        assertEquals(1, test.get(0).getConfirmedRequests());
        assertEquals(timestamp, test.get(0).getCreatedOn());
        assertEquals("test", test.get(0).getDescription());
        assertEquals(timestampEventDate, test.get(0).getEventDate());
        assertNotNull(test.get(0).getInitiator());
        assertNotNull(test.get(0).getLocation());
        assertTrue(test.get(0).isPaid());
        assertEquals(2, test.get(0).getParticipantLimit());
        assertEquals(timestamp, test.get(0).getPublishedOn());
        assertTrue(test.get(0).isRequestModeration());
        assertEquals(EventState.PENDING, test.get(0).getState());
        assertEquals("test", test.get(0).getTitle());
        assertEquals(1, test.get(0).getViews());

        verify(eventRepository, times(1)).findAllEventByInitiatorId(anyLong(), any(PageRequest.class));
    }

    @Test
    @DisplayName("Проверяем метод findEventByUser сервиса EventService.")
    void findEventByUserMethodTest() {
        when(eventRepository.findEventByIdAndInitiatorId(anyLong(), anyLong())).thenReturn(event);

        //test
        Event test = eventService.findEventByUser(1L, 1L);

        assertEquals(1L, test.getId());
        assertEquals("test", test.getAnnotation());
        assertNotNull(test.getCategory());
        assertEquals(1, test.getConfirmedRequests());
        assertEquals(timestamp, test.getCreatedOn());
        assertEquals("test", test.getDescription());
        assertEquals(timestampEventDate, test.getEventDate());
        assertNotNull(test.getInitiator());
        assertNotNull(test.getLocation());
        assertTrue(test.isPaid());
        assertEquals(2, test.getParticipantLimit());
        assertEquals(timestamp, test.getPublishedOn());
        assertTrue(test.isRequestModeration());
        assertEquals(EventState.PENDING, test.getState());
        assertEquals("test", test.getTitle());
        assertEquals(1, test.getViews());

        verify(eventRepository, times(1)).findEventByIdAndInitiatorId(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Проверяем метод cancelEvent сервиса EventService.")
    void cancelEventMethodTest() {
        when(eventRepository.findByIdAndInitiatorIdAndState(anyLong(), anyLong(), any(EventState.class))).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        //test
        Event test = eventService.cancelEvent(1L, 1L);

        assertEquals(1L, test.getId());
        assertEquals("test", test.getAnnotation());
        assertNotNull(test.getCategory());
        assertEquals(1, test.getConfirmedRequests());
        assertEquals(timestamp, test.getCreatedOn());
        assertEquals("test", test.getDescription());
        assertEquals(timestampEventDate, test.getEventDate());
        assertNotNull(test.getInitiator());
        assertNotNull(test.getLocation());
        assertTrue(test.isPaid());
        assertEquals(2, test.getParticipantLimit());
        assertEquals(timestamp, test.getPublishedOn());
        assertTrue(test.isRequestModeration());
        assertEquals(EventState.CANCELED, test.getState());
        assertEquals("test", test.getTitle());
        assertEquals(1, test.getViews());

        verify(eventRepository, times(1)).findByIdAndInitiatorIdAndState(anyLong(), anyLong(), any(EventState.class));
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    @DisplayName("Проверяем метод eventSearchPublic сервиса EventService.")
    void eventSearchPublicMethodTest() {
        when(eventRepository.eventPublicSearch(any(PublicEventSearchRequest.class))).thenReturn(List.of(event));

        PublicEventSearchRequest publicEventSearchRequest = PublicEventSearchRequest.builder().build();

        //test
        List<Event> test = eventService.eventSearchPublic(publicEventSearchRequest);

        assertEquals(1, test.size());
        assertEquals(1L, test.get(0).getId());
        assertEquals("test", test.get(0).getAnnotation());
        assertNotNull(test.get(0).getCategory());
        assertEquals(1, test.get(0).getConfirmedRequests());
        assertEquals(timestamp, test.get(0).getCreatedOn());
        assertEquals("test", test.get(0).getDescription());
        assertEquals(timestampEventDate, test.get(0).getEventDate());
        assertNotNull(test.get(0).getInitiator());
        assertNotNull(test.get(0).getLocation());
        assertTrue(test.get(0).isPaid());
        assertEquals(2, test.get(0).getParticipantLimit());
        assertEquals(timestamp, test.get(0).getPublishedOn());
        assertTrue(test.get(0).isRequestModeration());
        assertEquals(EventState.PENDING, test.get(0).getState());
        assertEquals("test", test.get(0).getTitle());
        assertEquals(2, test.get(0).getViews());

        verify(eventRepository, times(1)).eventPublicSearch(any(PublicEventSearchRequest.class));
    }

    @Test
    @DisplayName("Проверяем метод eventSearchAdmin сервиса EventService.")
    void eventSearchAdminMethodTest() {
        when(eventRepository.findAllByInitiatorIdInAndStateAndCategoryIdInAndEventDateBetween(
                anySet(), any(EventState.class), anySet(), any(Timestamp.class), any(Timestamp.class), any(PageRequest.class)))
                .thenReturn(List.of(event));

        AdminEventSearchRequest adminEventSearchRequest = AdminEventSearchRequest.builder()
                .fromPage(0)
                .sizePage(10)
                .users(Set.of(1L))
                .eventState(EventState.PENDING)
                .categories(Set.of(1L))
                .rangeStart(Timestamp.from(Instant.now()))
                .rangeEnd(Timestamp.from(Instant.now()))
                .build();

        //test
        List<Event> test = eventService.eventSearchAdmin(adminEventSearchRequest);

        assertEquals(1, test.size());
        assertEquals(1L, test.get(0).getId());
        assertEquals("test", test.get(0).getAnnotation());
        assertNotNull(test.get(0).getCategory());
        assertEquals(1, test.get(0).getConfirmedRequests());
        assertEquals(timestamp, test.get(0).getCreatedOn());
        assertEquals("test", test.get(0).getDescription());
        assertEquals(timestampEventDate, test.get(0).getEventDate());
        assertNotNull(test.get(0).getInitiator());
        assertNotNull(test.get(0).getLocation());
        assertTrue(test.get(0).isPaid());
        assertEquals(2, test.get(0).getParticipantLimit());
        assertEquals(timestamp, test.get(0).getPublishedOn());
        assertTrue(test.get(0).isRequestModeration());
        assertEquals(EventState.PENDING, test.get(0).getState());
        assertEquals("test", test.get(0).getTitle());
        assertEquals(1, test.get(0).getViews());

        verify(eventRepository, times(1)).findAllByInitiatorIdInAndStateAndCategoryIdInAndEventDateBetween(
                anySet(), any(EventState.class), anySet(), any(Timestamp.class), any(Timestamp.class), any(PageRequest.class));
    }

    @Test
    @DisplayName("Проверяем метод editEvent сервиса EventService.")
    void editEventMethodTest() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        AdminUpdateEventRequest adminUpdateEventRequest = AdminUpdateEventRequest.builder()
                .annotation("testtest")
                .paid(true)
                .requestModeration(true)
                .build();

        //test
        Event test = eventService.editEvent(adminUpdateEventRequest, 1L);

        assertEquals(1L, test.getId());
        assertEquals("testtest", test.getAnnotation());
        assertNotNull(test.getCategory());
        assertEquals(1, test.getConfirmedRequests());
        assertEquals(timestamp, test.getCreatedOn());
        assertEquals("test", test.getDescription());
        assertEquals(timestampEventDate, test.getEventDate());
        assertNotNull(test.getInitiator());
        assertNotNull(test.getLocation());
        assertTrue(test.isPaid());
        assertEquals(2, test.getParticipantLimit());
        assertEquals(timestamp, test.getPublishedOn());
        assertTrue(test.isRequestModeration());
        assertEquals(EventState.PENDING, test.getState());
        assertEquals("test", test.getTitle());
        assertEquals(1, test.getViews());

        verify(eventRepository, times(1)).findById(anyLong());
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    @DisplayName("Проверяем метод publishEvent сервиса EventService.")
    void publishEventMethodTest() {
        when(eventRepository.findEventByIdAndStateAndEventDateAfter(anyLong(), any(EventState.class), any(Timestamp.class))).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        //test
        Event test = eventService.publishEvent(1L);

        assertEquals(1L, test.getId());
        assertEquals("test", test.getAnnotation());
        assertNotNull(test.getCategory());
        assertEquals(1, test.getConfirmedRequests());
        assertEquals(timestamp, test.getCreatedOn());
        assertEquals("test", test.getDescription());
        assertEquals(timestampEventDate, test.getEventDate());
        assertNotNull(test.getInitiator());
        assertNotNull(test.getLocation());
        assertTrue(test.isPaid());
        assertEquals(2, test.getParticipantLimit());
        assertEquals(timestamp, test.getPublishedOn());
        assertTrue(test.isRequestModeration());
        assertEquals(EventState.PUBLISHED, test.getState());
        assertEquals("test", test.getTitle());
        assertEquals(1, test.getViews());

        verify(eventRepository, times(1)).findEventByIdAndStateAndEventDateAfter(anyLong(), any(EventState.class), any(Timestamp.class));
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    @DisplayName("Проверяем метод rejectEvent сервиса EventService.")
    void rejectEventMethodTest() {
        when(eventRepository.findEventByIdAndStateNot(anyLong(), any(EventState.class))).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        //test
        Event test = eventService.rejectEvent(1L);

        assertEquals(1L, test.getId());
        assertEquals("test", test.getAnnotation());
        assertNotNull(test.getCategory());
        assertEquals(1, test.getConfirmedRequests());
        assertEquals(timestamp, test.getCreatedOn());
        assertEquals("test", test.getDescription());
        assertEquals(timestampEventDate, test.getEventDate());
        assertNotNull(test.getInitiator());
        assertNotNull(test.getLocation());
        assertTrue(test.isPaid());
        assertEquals(2, test.getParticipantLimit());
        assertEquals(timestamp, test.getPublishedOn());
        assertTrue(test.isRequestModeration());
        assertEquals(EventState.CANCELED, test.getState());
        assertEquals("test", test.getTitle());
        assertEquals(1, test.getViews());

        verify(eventRepository, times(1)).findEventByIdAndStateNot(anyLong(), any(EventState.class));
        verify(eventRepository, times(1)).save(any(Event.class));
    }
}
