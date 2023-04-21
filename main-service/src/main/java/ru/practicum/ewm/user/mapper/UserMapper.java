package ru.practicum.ewm.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserTop;
import ru.practicum.ewm.user.model.User;

import java.util.List;

@Mapper
public interface UserMapper {

    @Mapping(target = "rate", ignore = true)
    User toUser(UserDto userDto);

    UserDto toUserDto(User user);

    List<UserDto> toUserDtoList(List<User> users);

    List<UserTop> toUserTopList(List<User> users);

}
