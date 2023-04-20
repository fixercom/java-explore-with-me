package ru.practicum.ewm.compilation.updater;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.ewm.compilation.model.Compilation;

@Mapper
public interface CompilationUpdater {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(Compilation sourceCompilation, @MappingTarget Compilation destinationCompilation);

}
