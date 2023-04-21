package ru.practicum.ewm.vote.service;

import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.vote.model.UserVote;

public interface UserVoteService {

    UserVote addUserVoteForEvent(User user, Event event, Boolean isPositive);

    void deleteUserVoteForEvent(User user, Event event, Boolean isPositive);

}
