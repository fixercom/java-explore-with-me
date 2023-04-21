package ru.practicum.ewm.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.not_found.UserNotFoundException;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public User createUser(User user) {
        user.setRate(0);
        userRepository.save(user);
        log.debug("User saved in the database, generated id={}", user.getId());
        return user;
    }

    @Override
    public User getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        log.debug("User with id={} was obtained from the database: {}", user.getId(), user);
        return user;
    }

    @Override
    public List<User> getUsers(List<Long> ids, Integer from, Integer size) {
        List<User> users;
        if (ids != null) {
            users = userRepository.findAllByIdIn(ids);
        } else {
            Pageable pageable = PageRequest.of(from / size, size);
            users = userRepository.findAll(pageable).getContent();
        }
        log.debug("Users were obtained from the database: {}", users);
        return users;
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        throwExceptionIfUserDoesNotExist(id);
        userRepository.deleteById(id);
        log.debug("User with id={} removed from the database", id);
    }

    @Override
    public void throwExceptionIfUserDoesNotExist(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
    }

    @Override
    public List<User> getTopUsers(Integer limit) {
        Pageable page = PageRequest.of(0, limit, Sort.by("rate").descending());
        List<User> users = userRepository.findAll(page).getContent();
        log.debug("User top with limit={} was obtained from the database", limit);
        return users;
    }

}
