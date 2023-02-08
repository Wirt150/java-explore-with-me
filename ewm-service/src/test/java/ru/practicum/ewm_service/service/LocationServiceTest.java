package ru.practicum.ewm_service.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.ewm_service.entity.Location;
import ru.practicum.ewm_service.repository.LocationRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class LocationServiceTest {
    @Autowired
    private LocationService locationService;
    @MockBean
    private LocationRepository locationRepository;
    private Location location;

    @BeforeEach
    void setUp() {
        location = Location.builder()
                .id(1L)
                .lon(1)
                .lat(1)
                .build();
    }

    @AfterEach
    void mockVerify() {
        verifyNoMoreInteractions(
                locationRepository
        );
    }

    @Test
    @DisplayName("Проверяем метод createRequest сервиса LocationService.")
    void createRequestMethodTest() {
        when(locationRepository.save(any(Location.class))).thenReturn(location);

        //test
        Location test = locationService.create(location);

        assertEquals(1L, test.getId());
        assertEquals(1, test.getLat());
        assertEquals(1, test.getLon());

        verify(locationRepository, times(1)).save(any(Location.class));
    }

}
