package ru.practicum.ewm_service.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.ewm_service.entity.*;
import ru.practicum.ewm_service.entity.constant.EventState;
import ru.practicum.ewm_service.repository.CompilationsRepository;
import ru.practicum.ewm_service.service.admin.AdminCompilationService;
import ru.practicum.ewm_service.service.admin.AdminEventService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class CompilationServiceTest {
    private final Timestamp timestamp = Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 1, 1));
    private final Timestamp timestampEventDate = Timestamp.valueOf(LocalDateTime.of(2024, 1, 1, 1, 1));
    @Autowired
    private AdminCompilationService compilationService;
    @MockBean
    private CompilationsRepository compilationsRepository;
    @MockBean
    private AdminEventService eventService;
    private Compilation compilation;
    private Event event;

    @BeforeEach
    void setUp() {
        compilation = Compilation.builder()
                .id(1L)
                .pinned(true)
                .title("test")
                .events(List.of(Event.builder().build()))
                .build();
        event = Event.builder()
                .id(1L)
                .annotation("test")
                .category(Category.builder().id(1L).build())
                .confirmedRequests(1)
                .createdOn(timestamp)
                .description("test")
                .eventDate(timestampEventDate)
                .initiator(User.builder().build())
                .location(Location.builder().build())
                .paid(true)
                .participantLimit(2)
                .publishedOn(timestamp)
                .requestModeration(true)
                .state(EventState.PENDING)
                .title("test")
                .views(1)
                .build();
    }

    @AfterEach
    void mockVerify() {
        verifyNoMoreInteractions(
                compilationsRepository,
                eventService
        );
    }

    @Test
    @DisplayName("Проверяем метод createCompilation сервиса CompilationService.")
    void createCompilationMethodTest() {
        when(eventService.findEvents(anyList())).thenReturn(List.of(event));
        when(compilationsRepository.save(any(Compilation.class))).thenReturn(compilation);

        //test
        Compilation test = compilationService.createCompilation(compilation);
        assertEquals(1L, test.getId());
        assertEquals("test", test.getTitle());
        assertEquals(1, test.getEvents().size());

        verify(eventService, times(1)).findEvents(anyList());
        verify(compilationsRepository, times(1)).save(any(Compilation.class));
    }

    @Test
    @DisplayName("Проверяем метод deleteCompilation сервиса CompilationService.")
    void deleteCompilationMethodTest() {
        //test
        compilationService.deleteCompilation(1L);

        verify(compilationsRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Проверяем метод deleteEventCompilation сервиса CompilationService.")
    void deleteEventCompilationMethodTest() {
        //test
        compilationService.deleteEventCompilation(1L, 1L);

        verify(compilationsRepository, times(1)).deleteEventsCompilations(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Проверяем метод editEventCompilation сервиса CompilationService.")
    void editEventCompilationMethodTest() {
        //test
        compilationService.editEventCompilation(1L, 1L);

        verify(compilationsRepository, times(1)).editEventsCompilations(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Проверяем метод deletePinCompilation сервиса CompilationService.")
    void deletePinCompilationMethodTest() {
        //test
        compilationService.deletePinCompilation(1L);

        verify(compilationsRepository, times(1)).deletePinCompilations(anyLong());
    }

    @Test
    @DisplayName("Проверяем метод editPinCompilation сервиса CompilationService.")
    void editPinCompilationMethodTest() {
        //test
        compilationService.editPinCompilation(1L);

        verify(compilationsRepository, times(1)).editPinCompilations(anyLong());
    }

    @Test
    @DisplayName("Проверяем метод getCompilations сервиса CompilationService.")
    void getCompilationsMethodTest() {
        when(compilationsRepository.findAllByPinned(anyBoolean(), any(PageRequest.class))).thenReturn(List.of(compilation));

        //test
        List<Compilation> test = compilationService.getCompilations(true, 0, 10);
        assertEquals(1L, test.get(0).getId());
        assertEquals("test", test.get(0).getTitle());
        assertEquals(1, test.get(0).getEvents().size());

        verify(compilationsRepository, times(1)).findAllByPinned(anyBoolean(), any(PageRequest.class));
    }

    @Test
    @DisplayName("Проверяем метод getCompilation сервиса CompilationService.")
    void getCompilationMethodTest() {
        when(compilationsRepository.findById(anyLong())).thenReturn(Optional.of(compilation));

        //test
        Compilation test = compilationService.getCompilation(1);
        assertEquals(1L, test.getId());
        assertEquals("test", test.getTitle());
        assertEquals(1, test.getEvents().size());

        verify(compilationsRepository, times(1)).findById(anyLong());
    }
}
