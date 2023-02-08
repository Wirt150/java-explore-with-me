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
import ru.practicum.ewm_service.entity.User;
import ru.practicum.ewm_service.entity.mapper.UserMapper;
import ru.practicum.ewm_service.entity.model.user.UserDto;
import ru.practicum.ewm_service.service.admin.AdminUserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminUserController.class)
public class AdminUserControllerTest {
    @MockBean
    private AdminUserService adminUserService;
    @MockBean
    private UserMapper userMapper;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("test")
                .email("test@test.test")
                .build();
        userDto = UserDto.builder()
                .id(1L)
                .name("test")
                .email("test@test.test")
                .build();
    }

    @AfterEach
    void mockVerify() {
        verifyNoMoreInteractions(
                adminUserService,
                userMapper
        );
    }

    @Test
    @DisplayName("Проверяем эндпоинт createUser контроллера AdminUserController.")
    void createUserTest() throws Exception {
        when(userMapper.toUser(any(UserDto.class))).thenReturn(user);
        when(adminUserService.create(any(User.class))).thenReturn(user);
        when(userMapper.toUserDto(any(User.class))).thenReturn(userDto);

        //test
        mvc.perform(post("/admin/users/")
                        .content(mapper.writeValueAsString(userDto))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));

        verify(userMapper, times(1)).toUser(any(UserDto.class));
        verify(adminUserService, times(1)).create(any(User.class));
        verify(userMapper, times(1)).toUserDto(any(User.class));
    }

    @Test
    @DisplayName("Проверяем эндпоинт deleteUser контроллера AdminUserController.")
    void deleteUserTest() throws Exception {
        //test
        mvc.perform(delete("/admin/users/1")
                        .param("userId", "1")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());

        verify(adminUserService, times(1)).delete(anyLong());
    }

    @Test
    @DisplayName("Проверяем эндпоинт findAllUserPageable контроллера AdminUserController.")
    void findAllUserPageableTest() throws Exception {
        when(adminUserService.findAllUser(anySet(), anyInt(), anyInt())).thenReturn(List.of(user));
        when(userMapper.toUserDtos(anyList())).thenReturn(List.of(userDto));

        //test
        mvc.perform(get("/admin/users/")
                        .param("from", "0")
                        .param("size", "10")
                        .param("ids", "1")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(userDto.getName())))
                .andExpect(jsonPath("$[0].email", is(userDto.getEmail())));

        verify(adminUserService, times(1)).findAllUser(anySet(), anyInt(), anyInt());
        verify(userMapper, times(1)).toUserDtos(anyList());
    }
}
