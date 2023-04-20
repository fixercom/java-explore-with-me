package ru.practicum.ewm.category.updater;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.ewm.category.model.Category;

@Mapper
public interface CategoryUpdater {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Category update(Category sourceCategory, @MappingTarget Category destinationCategory);

}
