package ru.practicum.ewm_service.web.admins;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Контроллер пользователей (Админ)", description = "Управление пользователями администратором")
@ApiResponses(
        value = {
                @ApiResponse(responseCode = "200", description = "Создано"),
                @ApiResponse(responseCode = "400", description = "Ошибка валидации входных данных"),
                @ApiResponse(responseCode = "404", description = "Не найденный объект"),
                @ApiResponse(responseCode = "403", description = "Доступ к ресурсу рграничен"),
                @ApiResponse(responseCode = "409", description = "Не уникальное поле в базе данных"),
                @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка"),
        }
)
public class AdminUserController {
    private final AdminUserService adminUserService;
    private final UserMapper userMapper;

    @Autowired
    public AdminUserController(AdminUserService adminUserService, UserMapper userMapper) {
        this.adminUserService = adminUserService;
        this.userMapper = userMapper;
    }

    @Operation(
            summary = "Создание нового пользователя (Admin)",
            description = "Создает нового пользователя и присваивает id"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возвращает созданного пользователя",
            content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserDto.class))
                    )
            })
    @PostMapping()
    public ResponseEntity<UserDto> createUser(
            @Parameter(description = "Dto пользователя") @Valid @RequestBody final UserDto dto
    ) {
        User user = adminUserService.create(userMapper.toUser(dto));
        return new ResponseEntity<>(userMapper.toUserDto(user), HttpStatus.OK);
    }

    @Operation(summary = "Удаление пользователя по его id (Admin)")
    @ApiResponse(
            responseCode = "200",
            description = "Удаляет пользователя и ничего не возвращает"
    )
    @DeleteMapping("{userId}")
    public ResponseEntity<UserDto> deleteUser(
            @Parameter(description = "Id пользователя") @PathVariable("userId") final long id
    ) {
        adminUserService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Поиск пользователей по их id (Admin)",
            description = "Ищет и возвращает списиок пользователей согласно параметрам"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возращает псотранично найденный список",
            content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserDto.class))
                    )
            })
    @GetMapping()
    public ResponseEntity<List<UserDto>> findAllUserPageable(
            @Parameter(description = "Число страниц") @RequestParam(defaultValue = "0") final int from,
            @Parameter(description = "Количество объектов") @RequestParam(defaultValue = "10") final int size,
            @Parameter(description = "Id пользователей") @RequestParam(value = "ids", required = false) final Set<Long> ids
    ) {
        return new ResponseEntity<>(userMapper.toUserDtos(adminUserService.findAllUser(ids, from, size)), HttpStatus.OK);
    }
}
