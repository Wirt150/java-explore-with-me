package ru.practicum.ewm_service.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.ewm_service.entity.Request;
import ru.practicum.ewm_service.entity.constant.RequestStatus;

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
        "classpath:sql_scripts/sql_scripts_request_create.sql"},
        executionPhase = BEFORE_TEST_METHOD)
public class RequestRepositoryTest {
    @Autowired
    private RequestRepository requestRepository;

    @Test
    @DisplayName("Проверяем кастомный метод existsByEventIdAndRequesterId репозитория RequestRepository")
    void existsByEventIdAndRequesterIdRepositoryMethodTest() {
        boolean test = requestRepository.existsByEventIdAndRequesterId(1L, 1L);

        //test
        assertThat(test, equalTo(true));
    }

    @Test
    @DisplayName("Проверяем кастомный метод findAllByEventId репозитория RequestRepository")
    void findAllByEventIdRepositoryMethodTest() {
        Integer test = requestRepository.countAllByEventId(1L);

        //test
        assertThat(test, equalTo(1));
    }

    @Test
    @DisplayName("Проверяем кастомный метод findRequestByIdAndRequesterId репозитория RequestRepository")
    void findRequestByIdAndRequesterIdMethodTest() {
        Optional<Request> test = requestRepository.findRequestByIdAndRequesterId(1L, 1L);

        //test
        assertThat(test, notNullValue());
        assertThat(test.isPresent(), equalTo(true));

        Request request = test.get();

        //test
        assertThat(request.getId(), equalTo(1L));
        assertThat(request.getCreated(), equalTo(Timestamp.valueOf("2022-01-01 00:00:00.00")));
        assertThat(request.getEvent(), notNullValue());
        assertThat(request.getRequester(), notNullValue());
        assertThat(request.getStatus(), equalTo(RequestStatus.PENDING));
    }

    @Test
    @DisplayName("Проверяем кастомный метод findAllByRequesterId репозитория RequestRepository")
    void findAllByRequesterIdMethodTest() {
        List<Request> test = requestRepository.findAllByRequesterId(1L);

        //test
        assertThat(test, hasSize(1));

        Request request = test.get(0);

        //test
        assertThat(request.getId(), equalTo(1L));
        assertThat(request.getCreated(), equalTo(Timestamp.valueOf("2022-01-01 00:00:00.00")));
        assertThat(request.getEvent(), notNullValue());
        assertThat(request.getRequester(), notNullValue());
        assertThat(request.getStatus(), equalTo(RequestStatus.PENDING));
    }

    @Test
    @DisplayName("Проверяем кастомный метод findAllByEventIdAndEvent_InitiatorId репозитория RequestRepository")
    void findAllByEventIdAndEvent_InitiatorIdMethodTest() {
        List<Request> test = requestRepository.findAllByEventIdAndEvent_InitiatorId(1L, 1L);

        //test
        assertThat(test, hasSize(1));

        Request request = test.get(0);

        //test
        assertThat(request.getId(), equalTo(1L));
        assertThat(request.getCreated(), equalTo(Timestamp.valueOf("2022-01-01 00:00:00.00")));
        assertThat(request.getEvent(), notNullValue());
        assertThat(request.getRequester(), notNullValue());
        assertThat(request.getStatus(), equalTo(RequestStatus.PENDING));
    }

    @Test
    @Transactional
    @DisplayName("Проверяем кастомный метод updateCanceledAllRequest репозитория RequestRepository")
    void updateCanceledAllRequestMethodTest() {
        requestRepository.updateCanceledAllRequest(1L);
        List<Request> test = requestRepository.findAllById(List.of(1L));

        //test
        assertThat(test.get(0).getStatus(), equalTo(RequestStatus.CANCELED));
    }
}
