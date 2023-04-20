package ru.practicum.ewm.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.PublicCompilationFilter;
import ru.practicum.ewm.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.compilation.service.CompilationService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Slf4j
public class CompilationPublicController {

    private final CompilationService compilationService;
    private final CompilationMapper compilationMapper;

    @GetMapping
    public List<CompilationDto> searchCompilations(@RequestParam(required = false) Boolean pinned,
                                                   @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                   @RequestParam(defaultValue = "10") @Positive Integer size,
                                                   HttpServletRequest request) {
        String params = request.getQueryString();
        if (params != null) {
            log.debug("{} request {}?{} received", request.getMethod(), request.getRequestURI(), params);
        } else {
            log.debug("{} request {} received", request.getMethod(), request.getRequestURI());
        }
        PublicCompilationFilter publicCompilationFilter = PublicCompilationFilter.builder()
                .pinned(pinned)
                .from(from)
                .size(size)
                .build();
        return compilationMapper.toCompilationDtoList(compilationService.searchCompilations(publicCompilationFilter));
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable Long compId, HttpServletRequest request) {
        log.debug("{} request {} received", request.getMethod(), request.getRequestURI());
        return compilationMapper.toCompilationDto(compilationService.getCompilationById(compId));
    }

}
