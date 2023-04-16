package ru.practicum.ewm.compilation.service;

import ru.practicum.ewm.compilation.dto.PublicCompilationFilter;
import ru.practicum.ewm.compilation.model.Compilation;

import java.util.List;

public interface CompilationService {

    Compilation createCompilation(Compilation compilation);

    void deleteCompilation(Long id);

    Compilation updateCompilation(Long id, Compilation sourceCompilation);

    List<Compilation> searchCompilations(PublicCompilationFilter publicCompilationFilter);

    Compilation getCompilationById(Long id);
}
