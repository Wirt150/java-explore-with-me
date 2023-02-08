package ru.practicum.ewm_service.web.publics;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ewm_service.entity.Compilation;
import ru.practicum.ewm_service.entity.Event;
import ru.practicum.ewm_service.entity.mapper.CompilationMapper;
import ru.practicum.ewm_service.entity.model.compilations.response.CompilationResponseDto;
import ru.practicum.ewm_service.entity.model.event.response.EventShortDto;
import ru.practicum.ewm_service.service.CompilationService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PublicCompilationController.class)
public class PublicCompilationControllerTest {
    @MockBean
    private CompilationService compilationService;
    @MockBean
    private CompilationMapper compilationMapper;
    @Autowired
    private MockMvc mvc;
    private Compilation compilation;
    private CompilationResponseDto compilationResponseDto;

    @BeforeEach
    void setUp() {
        compilation = Compilation.builder()
                .id(1L)
                .pinned(true)
                .title("test")
                .events(List.of(Event.builder().build()))
                .build();
        compilationResponseDto = CompilationResponseDto.builder()
                .id(1L)
                .pinned(true)
                .title("test")
                .events(List.of(EventShortDto.builder().build()))
                .build();
    }

    @AfterEach
    void mockVerify() {
        verifyNoMoreInteractions(
                compilationService,
                compilationMapper
        );
    }

    @Test
    @DisplayName("Проверяем эндпоинт getCompilation контроллера PublicCompilationController.")
    void getCompilationTest() throws Exception {
        when(compilationService.getCompilation(anyLong())).thenReturn(compilation);
        when(compilationMapper.toCompilationResponseDto(any(Compilation.class))).thenReturn(compilationResponseDto);

        //test
        mvc.perform(get("/compilations/1")
                        .param("compId", "1")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(compilationResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.pinned", is(true)))
                .andExpect(jsonPath("$.title", is(compilationResponseDto.getTitle())))
                .andExpect(jsonPath("$.events", hasSize(1)))
                .andExpect(jsonPath("$.events[0]", notNullValue()));

        verify(compilationService, times(1)).getCompilation(anyLong());
        verify(compilationMapper, times(1)).toCompilationResponseDto(any(Compilation.class));
    }

    @Test
    @DisplayName("Проверяем эндпоинт getCompilations контроллера PublicCompilationController.")
    void getCompilationsTest() throws Exception {
        when(compilationService.getCompilations(anyBoolean(), anyInt(), anyInt())).thenReturn(List.of(compilation));
        when(compilationMapper.toCompilationResponseDtos(anyList())).thenReturn(List.of(compilationResponseDto));

        //test
        mvc.perform(get("/compilations/")
                        .param("from", "1")
                        .param("size", "1")
                        .param("pinned", "true")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(compilationResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].pinned", is(true)))
                .andExpect(jsonPath("$[0].title", is(compilationResponseDto.getTitle())))
                .andExpect(jsonPath("$[0].events", hasSize(1)))
                .andExpect(jsonPath("$[0].events[0]", notNullValue()));

        verify(compilationService, times(1)).getCompilations(anyBoolean(), anyInt(), anyInt());
        verify(compilationMapper, times(1)).toCompilationResponseDtos(anyList());
    }
}
