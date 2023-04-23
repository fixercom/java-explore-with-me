package ru.practicum.ewm.vote.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.vote.model.UserVote;
import ru.practicum.ewm.vote.repository.UserVoteRepository;
import ru.practicum.ewm.vote.service.UserVoteService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserVoteServiceImpl implements UserVoteService {

    private final UserVoteRepository userVoteRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public UserVote addUserVoteForEvent(User user, Event event, Boolean isPositive) {
        return isPositive ? addLike(user, event) : addDislike(user, event);
    }

    @Override
    @Transactional
    public void deleteUserVoteForEvent(User user, Event event, Boolean isPositive) {
        Long userId = user.getId();
        Long eventId = event.getId();
        if (userVoteRepository.existsByUserIdAndEventIdAndIsPositive(userId, eventId, isPositive)) {
            userVoteRepository.deleteByUserIdAndEventIdAndIsPositive(userId, eventId, isPositive);
            if (isPositive) {
                event.setRate(event.getRate() - 1);
                event.setLikes(event.getLikes() - 1);
                event.getInitiator().setRate(event.getInitiator().getRate() - 1);
                log.debug("The like for the event with id={} was deleted by user with id={}", eventId, userId);
            } else {
                event.setRate(event.getRate() + 1);
                event.setDislikes(event.getDislikes() - 1);
                event.getInitiator().setRate(event.getInitiator().getRate() + 1);
                log.debug("The dislike for the event with id={} was deleted by user with id={}", eventId, userId);
            }
            eventRepository.save(event);
        }
    }

    private UserVote addLike(User user, Event event) {
        Long userId = user.getId();
        Long eventId = event.getId();
        UserVote userVote = new UserVote(null, userId, eventId, true);
        userVoteRepository.save(userVote);
        deleteDislikeIfExist(user, event);
        event.setRate(event.getRate() + 1);
        event.setLikes(event.getLikes() + 1);
        event.getInitiator().setRate(event.getInitiator().getRate() + 1);
        eventRepository.save(event);
        log.debug("The like for the event with id={} was added by user with id={}", eventId, userId);
        return userVote;
    }

    private UserVote addDislike(User user, Event event) {
        Long userId = user.getId();
        Long eventId = event.getId();
        UserVote userVote = new UserVote(null, userId, eventId, false);
        userVoteRepository.save(userVote);
        deleteLikeIfExist(user, event);
        event.setRate(event.getRate() - 1);
        event.setDislikes(event.getDislikes() + 1);
        event.getInitiator().setRate(event.getInitiator().getRate() - 1);
        eventRepository.save(event);
        log.debug("The dislike for the event with id={} was added by user with id={}", eventId, userId);
        return userVote;
    }

    private void deleteLikeIfExist(User user, Event event) {
        deleteUserVoteForEvent(user, event, true);
    }

    private void deleteDislikeIfExist(User user, Event event) {
        deleteUserVoteForEvent(user, event, false);
    }

}
