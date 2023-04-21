package ru.practicum.ewm.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.user.dto.UserTop;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserPublicController {

    private final UserMapper userMapper;
    private final UserService userService;

    @GetMapping("/top")
    List<UserTop> getTopEventInitiators(@RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer limit,
                                        HttpServletRequest request) {
        String params = request.getQueryString();
        if (params != null) {
            log.debug("{} request {}?{} received", request.getMethod(), request.getRequestURI(), params);
        } else {
            log.debug("{} request {} received", request.getMethod(), request.getRequestURI());
        }
        return userMapper.toUserTopList(userService.getTopUsers(limit));
    }

}
