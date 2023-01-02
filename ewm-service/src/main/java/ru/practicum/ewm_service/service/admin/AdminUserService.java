package ru.practicum.ewm_service.service.admin;

import ru.practicum.ewm_service.entity.User;
import ru.practicum.ewm_service.service.UserService;

import java.util.List;
import java.util.Set;

public interface AdminUserService extends UserService {

    User create(User user);

    List<User> findAllUser(Set<Long> ids, int from, int size);

    void delete(Long id);
}
