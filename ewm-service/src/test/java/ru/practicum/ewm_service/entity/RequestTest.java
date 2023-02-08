package ru.practicum.ewm_service.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.ewm_service.entity.constant.RequestStatus;
import ru.practicum.ewm_service.entity.model.request.RequestDto;

import java.sql.Timestamp;
import java.time.Instant;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class RequestTest {
    @Autowired
    private JacksonTester<Request> json;
    @Autowired
    private JacksonTester<RequestDto> jsonDto;

    @Test
    @DisplayName("Проверяем правильность сериализации Request.")
    void whenCreateRequestAndSerializableHim() throws Exception {

        final Request request = Request.builder()
                .id(1L)
                .event(Event.builder().id(1L).build())
                .requester(User.builder().id(1L).build())
                .status(RequestStatus.PENDING)
                .build();

        JsonContent<Request> result = json.write(request);

        //test
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.created").isNotBlank();
        assertThat(result).extractingJsonPathValue("$.event").extracting("id").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.requester").extracting("id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(RequestStatus.PENDING.toString());
    }

    @Test
    @DisplayName("Проверяем правильность сериализации RequestDto.")
    void whenCreateRequestDtoAndSerializableHim() throws Exception {

        final RequestDto requestDto = RequestDto.builder()
                .id(1L)
                .created(Timestamp.from(Instant.now()))
                .event(1)
                .requester(1)
                .status(RequestStatus.PENDING)
                .build();

        JsonContent<RequestDto> result = jsonDto.write(requestDto);

        //test
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.created").isNotBlank();
        assertThat(result).extractingJsonPathNumberValue("$.event").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.requester").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("PENDING");
    }
}
