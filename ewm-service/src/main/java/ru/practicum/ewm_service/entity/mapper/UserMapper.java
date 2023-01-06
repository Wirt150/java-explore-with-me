package ru.practicum.ewm_service.entity.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm_service.entity.User;
import ru.practicum.ewm_service.entity.model.user.UserDto;
import ru.practicum.ewm_service.entity.model.user.UserShortDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserDto userDto);

    UserDto toUserDto(User user);

    UserShortDto toUserShortDto(User user);

    List<UserDto> toUserDtos(List<User> users);
}
