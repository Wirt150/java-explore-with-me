package ru.practicum.ewm_service.web.admins;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_service.entity.User;
import ru.practicum.ewm_service.entity.mapper.UserMapper;
import ru.practicum.ewm_service.entity.model.user.UserDto;
import ru.practicum.ewm_service.service.admin.AdminUserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/users")
public class AdminUserController {
    private final AdminUserService adminUserService;
    private final UserMapper userMapper;

    @Autowired
    public AdminUserController(AdminUserService adminUserService, UserMapper userMapper) {
        this.adminUserService = adminUserService;
        this.userMapper = userMapper;
    }

    @GetMapping()
    public ResponseEntity<List<UserDto>> findAllUserPageable(
            @RequestParam(defaultValue = "0") final int from,
            @RequestParam(defaultValue = "10") final int size,
            @RequestParam(value = "ids", required = false) final Set<Long> ids
    ) {
        return new ResponseEntity<>(userMapper.toUserDtos(adminUserService.findAllUser(ids, from, size)), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<UserDto> createUser(
            @Valid @RequestBody final UserDto dto
    ) {
        User user = adminUserService.create(userMapper.toUser(dto));
        return new ResponseEntity<>(userMapper.toUserDto(user), HttpStatus.OK);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<UserDto> deleteUser(
            @PathVariable("userId") final long id
    ) {
        adminUserService.delete(id);
        return ResponseEntity.ok().build();
    }
}
