package ru.practicum.ewm.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @Email
    @NotBlank(message = "must not be blank")
    private String email;
    @Null(message = "id must be null")
    private Long id;
    @NotBlank(message = "must not be blank")
    private String name;

}
