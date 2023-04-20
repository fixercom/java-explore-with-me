package ru.practicum.ewm.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.service.CompilationService;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Slf4j
public class CompilationAdminController {

    private final CompilationService compilationService;
    private final EventService eventService;
    private final CompilationMapper compilationMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto,
                                            HttpServletRequest request) {
        log.debug("{} request {} received: {}", request.getMethod(), request.getRequestURI(), newCompilationDto);
        Compilation compilation = compilationMapper.toCompilation(newCompilationDto);
        List<Event> events = (newCompilationDto.getEvents() != null) ?
                eventService.getAllEventsByIdIn(newCompilationDto.getEvents()) : null;
        compilation.setEvents(events);
        return compilationMapper.toCompilationDto(compilationService.createCompilation(compilation));
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId, HttpServletRequest request) {
        log.debug("{} request {} received", request.getMethod(), request.getRequestURI());
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@PathVariable Long compId,
                                            @RequestBody @Valid UpdateCompilationRequest updateCompilationRequest,
                                            HttpServletRequest request) {
        log.debug("{} request {} received: {}", request.getMethod(), request.getRequestURI(), updateCompilationRequest);
        Compilation compilation = compilationMapper.toCompilation(updateCompilationRequest);
        List<Event> events = (updateCompilationRequest.getEvents() != null) ?
                eventService.getAllEventsByIdIn(updateCompilationRequest.getEvents()) : null;
        compilation.setEvents(events);
        return compilationMapper.toCompilationDto(compilationService.updateCompilation(compId, compilation));
    }


}
