package ru.practicum.ewm_service.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.ewm_service.entity.Event;
import ru.practicum.ewm_service.entity.constant.EventState;
import ru.practicum.ewm_service.entity.constant.SortState;
import ru.practicum.ewm_service.entity.model.event.request.PublicEventSearchRequest;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        "classpath:sql_scripts/sql_scripts_request_create.sql"},
        executionPhase = BEFORE_TEST_METHOD)
public class EventRepositoryTest {
    @Autowired
    private EventRepository eventRepository;

    @Test
    @DisplayName("Проверяем кастомный метод existsByCategoryId репозитория EventRepository")
    void existsByCategoryIdMethodTest() {
        boolean test = eventRepository.existsByCategoryId(1L);

        //test
        assertThat(test, equalTo(true));
    }

    @Test
    @DisplayName("Проверяем кастомный метод findEventByIdAndInitiatorId репозитория EventRepository")
    void findEventByIdAndInitiatorIdMethodTest() {
        Event test = eventRepository.findEventByIdAndInitiatorId(1L, 1L);

        //test
        assertThat(test, notNullValue());

        //test
        assertThat(test.getId(), equalTo(1L));
        assertThat(test.getAnnotation(), equalTo("testtesttesttesttesttest"));
        assertThat(test.getCategory(), notNullValue());
        assertThat(test.getConfirmedRequests(), equalTo(0));
        assertThat(test.getCreatedOn(), equalTo(Timestamp.valueOf("2022-01-01 00:00:00.00")));
        assertThat(test.getDescription(), equalTo("testtesttesttesttesttest"));
        assertThat(test.getEventDate(), equalTo(Timestamp.valueOf("2023-01-01 00:00:00.00")));
        assertThat(test.getCreatedOn(), equalTo(Timestamp.valueOf("2022-01-01 00:00:00.00")));
        assertThat(test.getInitiator(), notNullValue());
        assertThat(test.getLocation(), notNullValue());
        assertThat(test.isPaid(), equalTo(true));
        assertThat(test.getParticipantLimit(), equalTo(0));
        assertThat(test.getPublishedOn(), equalTo(Timestamp.valueOf("2022-01-01 00:00:00.00")));
        assertThat(test.isRequestModeration(), equalTo(true));
        assertThat(test.getState(), equalTo(EventState.PUBLISHED));
        assertThat(test.getTitle(), equalTo("test"));
        assertThat(test.getViews(), equalTo(1));
    }

    @Test
    @DisplayName("Проверяем кастомный метод findEventByIdAndState репозитория EventRepository")
    void findEventByIdAndStateMethodTest() {
        Optional<Event> testEvent = eventRepository.findEventByIdAndState(1L, EventState.PUBLISHED);

        //test
        assertThat(testEvent, notNullValue());
        assertThat(testEvent.isPresent(), equalTo(true));

        Event test = testEvent.get();

        //test
        assertThat(test.getId(), equalTo(1L));
        assertThat(test.getAnnotation(), equalTo("testtesttesttesttesttest"));
        assertThat(test.getCategory(), notNullValue());
        assertThat(test.getConfirmedRequests(), equalTo(0));
        assertThat(test.getCreatedOn(), equalTo(Timestamp.valueOf("2022-01-01 00:00:00.00")));
        assertThat(test.getDescription(), equalTo("testtesttesttesttesttest"));
        assertThat(test.getEventDate(), equalTo(Timestamp.valueOf("2023-01-01 00:00:00.00")));
        assertThat(test.getCreatedOn(), equalTo(Timestamp.valueOf("2022-01-01 00:00:00.00")));
        assertThat(test.getInitiator(), notNullValue());
        assertThat(test.getLocation(), notNullValue());
        assertThat(test.isPaid(), equalTo(true));
        assertThat(test.getParticipantLimit(), equalTo(0));
        assertThat(test.getPublishedOn(), equalTo(Timestamp.valueOf("2022-01-01 00:00:00.00")));
        assertThat(test.isRequestModeration(), equalTo(true));
        assertThat(test.getState(), equalTo(EventState.PUBLISHED));
        assertThat(test.getTitle(), equalTo("test"));
        assertThat(test.getViews(), equalTo(1));
    }

    @Test
    @DisplayName("Проверяем кастомный метод findByIdAndInitiatorIdAndStateNot репозитория EventRepository")
    void findByIdAndInitiatorIdAndStateNotMethodTest() {
        Optional<Event> testEvent = eventRepository.findByIdAndInitiatorIdAndStateNot(1L, 1L, EventState.CANCELED);

        //test
        assertThat(testEvent, notNullValue());
        assertThat(testEvent.isPresent(), equalTo(true));

        Event test = testEvent.get();

        //test
        assertThat(test.getId(), equalTo(1L));
        assertThat(test.getAnnotation(), equalTo("testtesttesttesttesttest"));
        assertThat(test.getCategory(), notNullValue());
        assertThat(test.getConfirmedRequests(), equalTo(0));
        assertThat(test.getCreatedOn(), equalTo(Timestamp.valueOf("2022-01-01 00:00:00.00")));
        assertThat(test.getDescription(), equalTo("testtesttesttesttesttest"));
        assertThat(test.getEventDate(), equalTo(Timestamp.valueOf("2023-01-01 00:00:00.00")));
        assertThat(test.getCreatedOn(), equalTo(Timestamp.valueOf("2022-01-01 00:00:00.00")));
        assertThat(test.getInitiator(), notNullValue());
        assertThat(test.getLocation(), notNullValue());
        assertThat(test.isPaid(), equalTo(true));
        assertThat(test.getParticipantLimit(), equalTo(0));
        assertThat(test.getPublishedOn(), equalTo(Timestamp.valueOf("2022-01-01 00:00:00.00")));
        assertThat(test.isRequestModeration(), equalTo(true));
        assertThat(test.getState(), equalTo(EventState.PUBLISHED));
        assertThat(test.getTitle(), equalTo("test"));
        assertThat(test.getViews(), equalTo(1));
    }

    @Test
    @DisplayName("Проверяем кастомный метод findByIdAndInitiatorIdAndState репозитория EventRepository")
    void findByIdAndInitiatorIdAndStateMethodTest() {
        Optional<Event> testEvent = eventRepository.findByIdAndInitiatorIdAndState(1L, 1L, EventState.PUBLISHED);

        //test
        assertThat(testEvent, notNullValue());
        assertThat(testEvent.isPresent(), equalTo(true));

        Event test = testEvent.get();

        //test
        assertThat(test.getId(), equalTo(1L));
        assertThat(test.getAnnotation(), equalTo("testtesttesttesttesttest"));
        assertThat(test.getCategory(), notNullValue());
        assertThat(test.getConfirmedRequests(), equalTo(0));
        assertThat(test.getCreatedOn(), equalTo(Timestamp.valueOf("2022-01-01 00:00:00.00")));
        assertThat(test.getDescription(), equalTo("testtesttesttesttesttest"));
        assertThat(test.getEventDate(), equalTo(Timestamp.valueOf("2023-01-01 00:00:00.00")));
        assertThat(test.getCreatedOn(), equalTo(Timestamp.valueOf("2022-01-01 00:00:00.00")));
        assertThat(test.getInitiator(), notNullValue());
        assertThat(test.getLocation(), notNullValue());
        assertThat(test.isPaid(), equalTo(true));
        assertThat(test.getParticipantLimit(), equalTo(0));
        assertThat(test.getPublishedOn(), equalTo(Timestamp.valueOf("2022-01-01 00:00:00.00")));
        assertThat(test.isRequestModeration(), equalTo(true));
        assertThat(test.getState(), equalTo(EventState.PUBLISHED));
        assertThat(test.getTitle(), equalTo("test"));
        assertThat(test.getViews(), equalTo(1));
    }

    @Test
    @DisplayName("Проверяем кастомный метод findAllEventByInitiatorId репозитория EventRepository")
    void findAllEventByInitiatorIdMethodTest() {
        List<Event> testEvent = eventRepository.findAllEventByInitiatorId(1L, PageRequest.of(0, 10));

        //test
        assertThat(testEvent, hasSize(1));

        Event test = testEvent.get(0);

        //test
        assertThat(test.getId(), equalTo(1L));
        assertThat(test.getAnnotation(), equalTo("testtesttesttesttesttest"));
        assertThat(test.getCategory(), notNullValue());
        assertThat(test.getConfirmedRequests(), equalTo(0));
        assertThat(test.getCreatedOn(), equalTo(Timestamp.valueOf("2022-01-01 00:00:00.00")));
        assertThat(test.getDescription(), equalTo("testtesttesttesttesttest"));
        assertThat(test.getEventDate(), equalTo(Timestamp.valueOf("2023-01-01 00:00:00.00")));
        assertThat(test.getCreatedOn(), equalTo(Timestamp.valueOf("2022-01-01 00:00:00.00")));
        assertThat(test.getInitiator(), notNullValue());
        assertThat(test.getLocation(), notNullValue());
        assertThat(test.isPaid(), equalTo(true));
        assertThat(test.getParticipantLimit(), equalTo(0));
        assertThat(test.getPublishedOn(), equalTo(Timestamp.valueOf("2022-01-01 00:00:00.00")));
        assertThat(test.isRequestModeration(), equalTo(true));
        assertThat(test.getState(), equalTo(EventState.PUBLISHED));
        assertThat(test.getTitle(), equalTo("test"));
        assertThat(test.getViews(), equalTo(1));
    }

    @Test
    @DisplayName("Проверяем кастомный метод findAllByInitiatorIdInAndStateAndCategoryIdInAndEventDateBetween репозитория EventRepository")
    void findAllByInitiatorIdInAndStateAndCategoryIdInAndEventDateBetweenMethodTest() {
        List<Event> testEvent = eventRepository.findAllByInitiatorIdInAndStateAndCategoryIdInAndEventDateBetween(
                Set.of(1L), EventState.PUBLISHED, Set.of(1L), Timestamp.valueOf("2022-01-01 00:00:00.00"),
                        Timestamp.valueOf("2024-01-01 00:00:00.00"), PageRequest.of(0, 10));

        //test
        assertThat(testEvent, hasSize(1));

        Event test = testEvent.get(0);

        //test
        assertThat(test.getId(), equalTo(1L));
        assertThat(test.getAnnotation(), equalTo("testtesttesttesttesttest"));
        assertThat(test.getCategory(), notNullValue());
        assertThat(test.getConfirmedRequests(), equalTo(0));
        assertThat(test.getCreatedOn(), equalTo(Timestamp.valueOf("2022-01-01 00:00:00.00")));
        assertThat(test.getDescription(), equalTo("testtesttesttesttesttest"));
        assertThat(test.getEventDate(), equalTo(Timestamp.valueOf("2023-01-01 00:00:00.00")));
        assertThat(test.getCreatedOn(), equalTo(Timestamp.valueOf("2022-01-01 00:00:00.00")));
        assertThat(test.getInitiator(), notNullValue());
        assertThat(test.getLocation(), notNullValue());
        assertThat(test.isPaid(), equalTo(true));
        assertThat(test.getParticipantLimit(), equalTo(0));
        assertThat(test.getPublishedOn(), equalTo(Timestamp.valueOf("2022-01-01 00:00:00.00")));
        assertThat(test.isRequestModeration(), equalTo(true));
        assertThat(test.getState(), equalTo(EventState.PUBLISHED));
        assertThat(test.getTitle(), equalTo("test"));
        assertThat(test.getViews(), equalTo(1));
    }

    @Test
    @DisplayName("Проверяем кастомный метод findEventByIdAndStateAndEventDateAfter репозитория EventRepository")
    void findEventByIdAndStateAndEventDateAfterMethodTest() {
        Optional<Event> testEvent = eventRepository.findEventByIdAndStateAndEventDateAfter(1L, EventState.PUBLISHED, Timestamp.valueOf("2022-01-01 00:00:00.00"));

        //test
        assertThat(testEvent, notNullValue());
        assertThat(testEvent.isPresent(), equalTo(true));

        Event test = testEvent.get();

        //test
        assertThat(test.getId(), equalTo(1L));
        assertThat(test.getAnnotation(), equalTo("testtesttesttesttesttest"));
        assertThat(test.getCategory(), notNullValue());
        assertThat(test.getConfirmedRequests(), equalTo(0));
        assertThat(test.getCreatedOn(), equalTo(Timestamp.valueOf("2022-01-01 00:00:00.00")));
        assertThat(test.getDescription(), equalTo("testtesttesttesttesttest"));
        assertThat(test.getEventDate(), equalTo(Timestamp.valueOf("2023-01-01 00:00:00.00")));
        assertThat(test.getCreatedOn(), equalTo(Timestamp.valueOf("2022-01-01 00:00:00.00")));
        assertThat(test.getInitiator(), notNullValue());
        assertThat(test.getLocation(), notNullValue());
        assertThat(test.isPaid(), equalTo(true));
        assertThat(test.getParticipantLimit(), equalTo(0));
        assertThat(test.getPublishedOn(), equalTo(Timestamp.valueOf("2022-01-01 00:00:00.00")));
        assertThat(test.isRequestModeration(), equalTo(true));
        assertThat(test.getState(), equalTo(EventState.PUBLISHED));
        assertThat(test.getTitle(), equalTo("test"));
        assertThat(test.getViews(), equalTo(1));
    }

    @Test
    @DisplayName("Проверяем кастомный метод findEventByIdAndStateNot репозитория EventRepository")
    void findEventByIdAndStateNotMethodTest() {
        Optional<Event> testEvent = eventRepository.findEventByIdAndStateNot(1L, EventState.CANCELED);

        //test
        assertThat(testEvent, notNullValue());
        assertThat(testEvent.isPresent(), equalTo(true));

        Event test = testEvent.get();

        //test
        assertThat(test.getId(), equalTo(1L));
        assertThat(test.getAnnotation(), equalTo("testtesttesttesttesttest"));
        assertThat(test.getCategory(), notNullValue());
        assertThat(test.getConfirmedRequests(), equalTo(0));
        assertThat(test.getCreatedOn(), equalTo(Timestamp.valueOf("2022-01-01 00:00:00.00")));
        assertThat(test.getDescription(), equalTo("testtesttesttesttesttest"));
        assertThat(test.getEventDate(), equalTo(Timestamp.valueOf("2023-01-01 00:00:00.00")));
        assertThat(test.getCreatedOn(), equalTo(Timestamp.valueOf("2022-01-01 00:00:00.00")));
        assertThat(test.getInitiator(), notNullValue());
        assertThat(test.getLocation(), notNullValue());
        assertThat(test.isPaid(), equalTo(true));
        assertThat(test.getParticipantLimit(), equalTo(0));
        assertThat(test.getPublishedOn(), equalTo(Timestamp.valueOf("2022-01-01 00:00:00.00")));
        assertThat(test.isRequestModeration(), equalTo(true));
        assertThat(test.getState(), equalTo(EventState.PUBLISHED));
        assertThat(test.getTitle(), equalTo("test"));
        assertThat(test.getViews(), equalTo(1));
    }

    @Test
    @DisplayName("Проверяем кастомный метод eventPublicSearch репозитория EventRepository")
    void eventPublicSearchMethodTest() {
        final PublicEventSearchRequest publicEventSearchRequest = PublicEventSearchRequest.builder()
                .fromPage(0)
                .sizePage(10)
                .text("test")
                .categories(Set.of(1L))
                .paid(true)
                .onlyAvailable(false)
                .sort(SortState.EVENT_DATE)
                .rangeStart(Timestamp.valueOf("2022-01-01 00:00:00.00"))
                .rangeEnd(Timestamp.valueOf("2024-01-01 00:00:00.00"))
                .build();

        List<Event> testEvent = eventRepository.eventPublicSearch(publicEventSearchRequest);

        //test
        assertThat(testEvent, hasSize(1));

        Event test = testEvent.get(0);

        //test
        assertThat(test.getId(), equalTo(1L));
        assertThat(test.getAnnotation(), equalTo("testtesttesttesttesttest"));
        assertThat(test.getCategory(), notNullValue());
        assertThat(test.getConfirmedRequests(), equalTo(0));
        assertThat(test.getCreatedOn(), equalTo(Timestamp.valueOf("2022-01-01 00:00:00.00")));
        assertThat(test.getDescription(), equalTo("testtesttesttesttesttest"));
        assertThat(test.getEventDate(), equalTo(Timestamp.valueOf("2023-01-01 00:00:00.00")));
        assertThat(test.getCreatedOn(), equalTo(Timestamp.valueOf("2022-01-01 00:00:00.00")));
        assertThat(test.getInitiator(), notNullValue());
        assertThat(test.getLocation(), notNullValue());
        assertThat(test.isPaid(), equalTo(true));
        assertThat(test.getParticipantLimit(), equalTo(0));
        assertThat(test.getPublishedOn(), equalTo(Timestamp.valueOf("2022-01-01 00:00:00.00")));
        assertThat(test.isRequestModeration(), equalTo(true));
        assertThat(test.getState(), equalTo(EventState.PUBLISHED));
        assertThat(test.getTitle(), equalTo("test"));
        assertThat(test.getViews(), equalTo(1));
    }
}

