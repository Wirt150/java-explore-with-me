package ru.practicum.ewm_service.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.ewm_service.entity.User;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@ActiveProfiles("test")
@SpringBootTest(
        properties = {
                "spring.datasource.driverClassName=org.h2.Driver",
                "spring.datasource.url=jdbc:h2:mem:ewm_service",
                "spring.datasource.username=test",
                "spring.datasource.password=test"
        },
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@Sql(scripts = {
        "classpath:schema.sql",
        "classpath:sql_scripts/sql_scripts_user_create.sql"},
        executionPhase = BEFORE_TEST_METHOD)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Проверяем кастомный метод findByIdIn репозитория userRepository")
    void findByIdInRepositoryMethodTest() {
        List<User> test = userRepository.findByIdIn(List.of(1L), PageRequest.of(0, 10));

        //test
        assertThat(test, hasSize(1));

        User user = test.get(0);

        //test
        assertThat(user.getId(), equalTo(1L));
        assertThat(user.getName(), equalTo("test"));
        assertThat(user.getEmail(), equalTo("test@test.test"));
    }
}
