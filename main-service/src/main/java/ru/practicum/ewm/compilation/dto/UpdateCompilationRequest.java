package ru.practicum.ewm.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.validation.annotation.NotBlankButMayBeNull;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompilationRequest {

    private List<Long> events;
    private Boolean pinned;
    @NotBlankButMayBeNull(message = "title must not be blank")
    private String title;

}
