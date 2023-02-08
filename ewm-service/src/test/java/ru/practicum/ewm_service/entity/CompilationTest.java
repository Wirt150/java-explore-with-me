package ru.practicum.ewm_service.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.ewm_service.entity.model.compilations.request.CompilationRequestDto;
import ru.practicum.ewm_service.entity.model.compilations.response.CompilationResponseDto;
import ru.practicum.ewm_service.entity.model.event.response.EventShortDto;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class CompilationTest {
    @Autowired
    private JacksonTester<Compilation> json;
    @Autowired
    private JacksonTester<CompilationResponseDto> jsonResp;
    @Autowired
    private JacksonTester<CompilationRequestDto> jsonReq;

    @Test
    @DisplayName("Проверяем правильность сериализации Compilation.")
    void whenCreateCompilationAndSerializableHim() throws Exception {

        final Compilation compilation = Compilation.builder()
                .id(1L)
                .pinned(true)
                .title("test")
                .events(Collections.singletonList(Event.builder().id(1L).build()))
                .build();

        JsonContent<Compilation> result = json.write(compilation);

        //test
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathBooleanValue("$.pinned").isTrue();
        assertThat(result).extractingJsonPathStringValue("$.title").isEqualTo("test");
        assertThat(result).extractingJsonPathArrayValue("$.events").size().isEqualTo(1);
    }

    @Test
    @DisplayName("Проверяем правильность сериализации CompilationResponseDto.")
    void whenCreateCompilationResponseDtoAndSerializableHim() throws Exception {

        final CompilationResponseDto compilationResponseDto = CompilationResponseDto.builder()
                .id(1L)
                .pinned(true)
                .title("test")
                .events(Collections.singletonList(EventShortDto.builder().id(1L).build()))
                .build();

        JsonContent<CompilationResponseDto> result = jsonResp.write(compilationResponseDto);

        //test
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathBooleanValue("$.pinned").isTrue();
        assertThat(result).extractingJsonPathStringValue("$.title").isEqualTo("test");
        assertThat(result).extractingJsonPathArrayValue("$.events").size().isEqualTo(1);
    }

    @Test
    @DisplayName("Проверяем правильность сериализации CompilationRequestDto.")
    void whenCreateCompilationRequestDtoAndSerializableHim() throws Exception {

        final CompilationRequestDto compilationRequestDto = CompilationRequestDto.builder()
                .pinned(true)
                .title("test")
                .events(Set.of(1L))
                .build();

        JsonContent<CompilationRequestDto> result = jsonReq.write(compilationRequestDto);

        //test
        assertThat(result).extractingJsonPathBooleanValue("$.pinned").isTrue();
        assertThat(result).extractingJsonPathStringValue("$.title").isEqualTo("test");
        assertThat(result).extractingJsonPathArrayValue("$.events").size().isEqualTo(1);
    }
}
