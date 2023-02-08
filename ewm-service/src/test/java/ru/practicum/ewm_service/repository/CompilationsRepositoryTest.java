package ru.practicum.ewm_service.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.ewm_service.entity.Compilation;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
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
        "classpath:sql_scripts/sql_scripts_compilations_create.sql"},
        executionPhase = BEFORE_TEST_METHOD)
@Transactional
public class CompilationsRepositoryTest {
    @Autowired
    private CompilationsRepository compilationsRepository;

    @Test
    @DisplayName("Проверяем кастомный метод deleteEventsCompilations репозитория CompilationsRepository")
    void deleteEventsCompilationsMethodTest() {
        compilationsRepository.deleteEventsCompilations(1L, 1L);

        Optional<Compilation> test = compilationsRepository.findById(1L);

        //test
        assertThat(test, notNullValue());
        assertThat(test.isPresent(), equalTo(true));

        Compilation request = test.get();

        //test
        assertThat(request.getId(), equalTo(1L));
        assertThat(request.isPinned(), equalTo(true));
        assertThat(request.getTitle(), equalTo("test"));
        assertThat(request.getEvents().size(), equalTo(0));
    }

    @Test
    @DisplayName("Проверяем кастомный метод editEventsCompilations репозитория CompilationsRepository")
    void editEventsCompilationsMethodTest() {
        compilationsRepository.deleteEventsCompilations(1L, 1L);
        compilationsRepository.editEventsCompilations(1L, 1L);

        Optional<Compilation> test = compilationsRepository.findById(1L);

        //test
        assertThat(test, notNullValue());
        assertThat(test.isPresent(), equalTo(true));

        Compilation request = test.get();

        //test
        assertThat(request.getId(), equalTo(1L));
        assertThat(request.isPinned(), equalTo(true));
        assertThat(request.getTitle(), equalTo("test"));
        assertThat(request.getEvents().size(), equalTo(1));
    }

    @Test
    @DisplayName("Проверяем кастомный метод deletePinCompilations репозитория CompilationsRepository")
    void deletePinCompilationsMethodTest() {
        compilationsRepository.deletePinCompilations(1L);

        Optional<Compilation> test = compilationsRepository.findById(1L);

        //test
        assertThat(test, notNullValue());
        assertThat(test.isPresent(), equalTo(true));

        Compilation request = test.get();

        //test
        assertThat(request.getId(), equalTo(1L));
        assertThat(request.isPinned(), equalTo(false));
        assertThat(request.getTitle(), equalTo("test"));
        assertThat(request.getEvents().size(), equalTo(1));
    }

    @Test
    @DisplayName("Проверяем кастомный метод editPinCompilations репозитория CompilationsRepository")
    void editPinCompilationsMethodTest() {
        compilationsRepository.deletePinCompilations(1L);
        compilationsRepository.editPinCompilations(1L);

        Optional<Compilation> test = compilationsRepository.findById(1L);

        //test
        assertThat(test, notNullValue());
        assertThat(test.isPresent(), equalTo(true));

        Compilation request = test.get();

        //test
        assertThat(request.getId(), equalTo(1L));
        assertThat(request.isPinned(), equalTo(true));
        assertThat(request.getTitle(), equalTo("test"));
        assertThat(request.getEvents().size(), equalTo(1));
    }
}
