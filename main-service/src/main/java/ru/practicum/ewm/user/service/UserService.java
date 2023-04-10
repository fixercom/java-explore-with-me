package ru.practicum.ewm.user.service;

import ru.practicum.ewm.user.model.User;

import java.util.List;

public interface UserService {

    User createUser(User user);

    List<User> getUsers(List<Long> ids, Integer from, Integer size);

    void deleteUser(Long id);

    void throwExceptionIfUserDoesNotExist(Long id);

}
