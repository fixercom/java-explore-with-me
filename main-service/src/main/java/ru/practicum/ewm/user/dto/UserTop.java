package ru.practicum.ewm.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserTop {

    private Long id;
    private String name;
    private Integer rate;

}
