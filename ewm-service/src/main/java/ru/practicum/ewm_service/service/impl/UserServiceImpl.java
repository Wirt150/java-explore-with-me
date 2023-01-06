package ru.practicum.ewm_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm_service.entity.User;
import ru.practicum.ewm_service.error.user.UserNotFoundException;
import ru.practicum.ewm_service.repository.UserRepository;
import ru.practicum.ewm_service.service.UserService;
import ru.practicum.ewm_service.service.admin.AdminUserService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, AdminUserService {
    private final UserRepository userRepository;

    @Override
    public User create(final User user) {
        return userRepository.save(user);
    }

    @Override
    public User getById(final Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllUser(final Set<Long> ids, final int from, final int size) {
        if (ids == null) {
            return userRepository.findAll(PageRequest.of(from, size)).stream().collect(Collectors.toList());
        }
        return userRepository.findByIdIn(ids, PageRequest.of(from, size));
    }

    @Override
    public void delete(final Long id) {
        userRepository.deleteById(id);
    }
}
