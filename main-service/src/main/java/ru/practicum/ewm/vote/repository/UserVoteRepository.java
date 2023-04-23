package ru.practicum.ewm.vote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.vote.model.UserVote;

public interface UserVoteRepository extends JpaRepository<UserVote, Long> {


    Boolean existsByUserIdAndEventIdAndIsPositive(Long userId, Long eventId, Boolean isPositive);

    void deleteByUserIdAndEventIdAndIsPositive(Long userId, Long eventId, Boolean isPositive);

}