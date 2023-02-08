package ru.practicum.ewm_service.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.ewm_service.entity.User;
import ru.practicum.ewm_service.repository.UserRepository;
import ru.practicum.ewm_service.service.admin.AdminUserService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {
    @Autowired
    private AdminUserService userService;
    @MockBean
    private UserRepository userRepository;
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("test")
                .email("test@test.test")
                .build();
    }

    @AfterEach
    void mockVerify() {
        verifyNoMoreInteractions(
                userRepository
        );
    }

    @Test
    @DisplayName("Проверяем метод create сервиса UserService.")
    void createMethodTest() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        //test
        User test = userService.create(user);
        assertEquals(1L, test.getId());
        assertEquals("test", test.getName());
        assertEquals("test@test.test", test.getEmail());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Проверяем метод getById сервиса UserService.")
    void getByIdMethodTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        //test
        User test = userService.getById(1L);
        assertEquals(1L, test.getId());
        assertEquals("test", test.getName());
        assertEquals("test@test.test", test.getEmail());

        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Проверяем метод findAllUser сервиса UserService.")
    void findAllUserMethodTest() {
        when(userRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(user)));
        when(userRepository.findByIdIn(anyCollection(), any(Pageable.class))).thenReturn(List.of(user));

        //test
        List<User> testNullIds = userService.findAllUser(null, 0, 10);
        assertEquals(1, testNullIds.size());
        assertEquals(1L, testNullIds.get(0).getId());
        assertEquals("test", testNullIds.get(0).getName());
        assertEquals("test@test.test", testNullIds.get(0).getEmail());

        //test
        List<User> test = userService.findAllUser(Set.of(1L), 0, 10);
        assertEquals(1, test.size());
        assertEquals(1L, test.get(0).getId());
        assertEquals("test", test.get(0).getName());
        assertEquals("test@test.test", test.get(0).getEmail());

        verify(userRepository, times(1)).findAll(any(Pageable.class));
        verify(userRepository, times(1)).findByIdIn(anyCollection(), any(Pageable.class));
    }

    @Test
    @DisplayName("Проверяем метод delete сервиса UserService.")
    void deleteMethodTest() {
        userService.delete(1L);

        //test
        verify(userRepository, times(1)).deleteById(anyLong());
    }
}
