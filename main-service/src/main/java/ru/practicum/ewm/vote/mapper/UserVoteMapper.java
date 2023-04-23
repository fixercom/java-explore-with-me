package ru.practicum.ewm.vote.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm.vote.dto.UserVoteDto;
import ru.practicum.ewm.vote.model.UserVote;

@Mapper
public interface UserVoteMapper {

    UserVoteDto toUserVoteDto(UserVote userVote);

}