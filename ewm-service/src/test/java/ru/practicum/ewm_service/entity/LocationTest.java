package ru.practicum.ewm_service.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class LocationTest {
    @Autowired
    private JacksonTester<Location> json;

    @Test
    @DisplayName("Проверяем правильность сериализации Location.")
    void whenCreateLocationAndSerializableHim() throws Exception {

        final Location location = Location.builder()
                .id(1L)
                .lat(1.0F)
                .lon(1.0F)
                .build();

        JsonContent<Location> result = json.write(location);

        //test
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.lat").isEqualTo(1.0);
        assertThat(result).extractingJsonPathNumberValue("$.lon").isEqualTo(1.0);
    }
}
