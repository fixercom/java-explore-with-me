package ru.practicum.ewm.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    @Null(message = "id must be null")
    private Long id;
    @NotBlank(message = "name must not be blank")
    private String name;

}
