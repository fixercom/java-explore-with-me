package ru.practicum.ewm.compilation.service.impl;

import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilation.dto.PublicCompilationFilter;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.model.QCompilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.compilation.service.CompilationService;
import ru.practicum.ewm.compilation.updater.CompilationUpdater;
import ru.practicum.ewm.exception.not_found.CompilationNotFoundException;
import ru.practicum.ewm.util.QPredicates;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final CompilationUpdater compilationUpdater;

    @Override
    @Transactional
    public Compilation createCompilation(Compilation compilation) {
        compilationRepository.save(compilation);
        log.debug("Compilation saved in the database, generated id={}", compilation.getId());
        return compilation;
    }

    @Override
    @Transactional
    public void deleteCompilation(Long id) {
        throwExceptionIfCompilationNotExist(id);
        compilationRepository.deleteById(id);
        log.debug("Compilation with id={} removed from the database", id);
    }

    @Override
    @Transactional
    public Compilation updateCompilation(Long id, Compilation sourceCompilation) {
        Compilation targetCompilation = getCompilationById(id);
        compilationUpdater.update(sourceCompilation, targetCompilation);
        compilationRepository.save(targetCompilation);
        log.debug("Compilation with id={} updated in the database: {}", id, targetCompilation);
        return targetCompilation;
    }

    @Override
    public List<Compilation> searchCompilations(PublicCompilationFilter publicCompilationFilter) {
        Integer from = publicCompilationFilter.getFrom();
        Integer size = publicCompilationFilter.getSize();
        Pageable page = PageRequest.of(from / size, size);
        Predicate predicate = QPredicates.builder()
                .add(publicCompilationFilter.getPinned(), QCompilation.compilation.pinned::eq)
                .buildAnd();
        List<Compilation> compilations = (predicate != null) ?
                compilationRepository.findAll(predicate, page).getContent() :
                compilationRepository.findAll(page).getContent();
        log.debug("Compilations were obtained from the database, quantity={}", compilations.size());
        return compilations;
    }

    @Override
    public Compilation getCompilationById(Long id) {
        Compilation compilation = compilationRepository.findById(id)
                .orElseThrow(() -> new CompilationNotFoundException(id));
        log.debug("Compilation with id={} was obtained from the database: {}", compilation.getId(), compilation);
        return compilation;
    }

    private void throwExceptionIfCompilationNotExist(Long id) {
        if (!compilationRepository.existsById(id)) {
            throw new CompilationNotFoundException(id);
        }
    }

}
