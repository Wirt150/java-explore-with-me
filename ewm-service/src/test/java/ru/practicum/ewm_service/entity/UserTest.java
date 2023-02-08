package ru.practicum.ewm_service.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.ewm_service.entity.model.user.UserDto;
import ru.practicum.ewm_service.entity.model.user.UserShortDto;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class UserTest {
    @Autowired
    private JacksonTester<User> json;
    @Autowired
    private JacksonTester<UserDto> jsonDto;
    @Autowired
    private JacksonTester<UserShortDto> jsonDtoShort;


    @Test
    @DisplayName("Проверяем правильность сериализации User.")
    void whenCreateUserAndSerializableHim() throws Exception {

        final User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .name("test")
                .build();

        JsonContent<User> result = json.write(user);

        //test
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("test@test.test");
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("test");
    }

    @Test
    @DisplayName("Проверяем правильность сериализации UserDto.")
    void whenCreateUserDtoAndSerializableHim() throws Exception {

        final UserDto userDto = UserDto.builder()
                .id(1L)
                .email("test@test.test")
                .name("test")
                .build();

        JsonContent<UserDto> result = jsonDto.write(userDto);

        //test
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("test@test.test");
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("test");
    }

    @Test
    @DisplayName("Проверяем правильность сериализации UserShortDto.")
    void whenCreateUserShortDtoAndSerializableHim() throws Exception {

        final UserShortDto userShortDto = UserShortDto.builder()
                .id(1L)
                .name("test")
                .build();

        JsonContent<UserShortDto> result = jsonDtoShort.write(userShortDto);

        //test
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("test");
    }
}
