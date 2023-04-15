package ru.practicum.ewm.category.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.category.service.CategoryService;
import ru.practicum.ewm.category.updater.CategoryUpdater;
import ru.practicum.ewm.exception.not_found.CategoryNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryUpdater categoryUpdater;

    @Override
    @Transactional
    public Category createCategory(Category category) {
        Category savedCategory = categoryRepository.save(category);
        log.debug("Category saved in the database, generated id={}", category.getId());
        return savedCategory;
    }

    @Override
    public Category getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
        log.debug("Category with id={} was obtained from the database: {}", id, category);
        return category;
    }

    @Override
    public List<Category> getAllCategories(Integer from, Integer size) {
        Pageable page = PageRequest.of(from / size, size);
        List<Category> categories = categoryRepository.findAll(page).getContent();
        log.debug("Categories was obtained from the database: {}", categories);
        return categories;
    }

    @Override
    @Transactional
    public Category updateCategory(Long id, Category sourceCategory) {
        Category destinationCategory = getCategoryById(id);
        Category categoryWithUpdatedFields = categoryUpdater.update(sourceCategory, destinationCategory);
        Category updatedCategory = categoryRepository.save(categoryWithUpdatedFields);
        log.debug("Category with id={} updated in the database: {}", id, updatedCategory);
        return updatedCategory;
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        throwExceptionIfCategoryDoesNotExist(id);
        categoryRepository.deleteById(id);
        log.debug("Category with id={} removed from the database", id);
    }

    private void throwExceptionIfCategoryDoesNotExist(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new CategoryNotFoundException(id);
        }
    }

}
