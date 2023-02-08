package ru.practicum.ewm_service.web.admins;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.practicum.ewm_service.entity.model.compilations.request.CompilationRequestDto;
import ru.practicum.ewm_service.entity.model.compilations.response.CompilationResponseDto;
import ru.practicum.ewm_service.entity.model.event.response.EventShortDto;
import ru.practicum.ewm_service.service.admin.AdminCompilationService;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminCompilationsController.class)
public class AdminCompilationsControllerTest {
    @MockBean
    private AdminCompilationService adminCompilationService;
    @MockBean
    private CompilationMapper compilationMapper;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    private Compilation compilation;
    private CompilationResponseDto compilationResponseDto;
    private CompilationRequestDto compilationRequestDto;

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
        compilationRequestDto = CompilationRequestDto.builder()
                .pinned(true)
                .events(Set.of(1L))
                .title("test")
                .build();
    }

    @AfterEach
    void mockVerify() {
        verifyNoMoreInteractions(
                adminCompilationService,
                compilationMapper
        );
    }

    @Test
    @DisplayName("Проверяем эндпоинт createCompilation контроллера AdminCompilationsController.")
    void createCompilationTest() throws Exception {
        when(compilationMapper.toCompilation(any(CompilationRequestDto.class))).thenReturn(compilation);
        when(adminCompilationService.createCompilation(any(Compilation.class))).thenReturn(compilation);
        when(compilationMapper.toCompilationResponseDto(any(Compilation.class))).thenReturn(compilationResponseDto);

        //test
        mvc.perform(post("/admin/compilations")
                        .content(mapper.writeValueAsString(compilationRequestDto))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(compilationResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.pinned", is(compilationResponseDto.isPinned())))
                .andExpect(jsonPath("$.title", is(compilationResponseDto.getTitle())))
                .andExpect(jsonPath("$.events[0]", notNullValue()));

        verify(compilationMapper, times(1)).toCompilation(any(CompilationRequestDto.class));
        verify(adminCompilationService, times(1)).createCompilation(any(Compilation.class));
        verify(compilationMapper, times(1)).toCompilationResponseDto(any(Compilation.class));
    }

    @Test
    @DisplayName("Проверяем эндпоинт createCompilation контроллера AdminCompilationsController.")
    void deleteUserTest() throws Exception {
        //test
        mvc.perform(delete("/admin/compilations/1")
                        .param("compId", "1")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());

        verify(adminCompilationService, times(1)).deleteCompilation(anyLong());
    }

    @Test
    @DisplayName("Проверяем эндпоинт deleteEventCompilation контроллера AdminCompilationsController.")
    void deleteEventCompilationTest() throws Exception {
        //test
        mvc.perform(delete("/admin/compilations/1/events/1")
                        .param("compId", "1")
                        .param("eventId", "1")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());

        verify(adminCompilationService, times(1)).deleteEventCompilation(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Проверяем эндпоинт editEventCompilation контроллера AdminCompilationsController.")
    void editEventCompilationTest() throws Exception {
        //test
        mvc.perform(patch("/admin/compilations/1/events/1")
                        .param("compId", "1")
                        .param("eventId", "1")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());

        verify(adminCompilationService, times(1)).editEventCompilation(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Проверяем эндпоинт deletePinCompilation контроллера AdminCompilationsController.")
    void deletePinCompilationTest() throws Exception {
        //test
        mvc.perform(delete("/admin/compilations/1/pin")
                        .param("compId", "1")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());

        verify(adminCompilationService, times(1)).deletePinCompilation(anyLong());
    }

    @Test
    @DisplayName("Проверяем эндпоинт editPinCompilation контроллера AdminCompilationsController.")
    void editPinCompilationTest() throws Exception {
        //test
        mvc.perform(patch("/admin/compilations/1/pin")
                        .param("compId", "1")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());

        verify(adminCompilationService, times(1)).editPinCompilation(anyLong());
    }

}
