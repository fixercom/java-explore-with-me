package ru.practicum.ewm.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.model.User;

import java.util.List;

@Mapper
public interface UserMapper {

    User toUser(UserDto userDto);

    UserDto toUserDto(User user);

    List<UserDto> toUserDtoList(List<User> users);

}
