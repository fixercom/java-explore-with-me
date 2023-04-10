package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.model.Category;

import java.util.List;

public interface CategoryService {

    Category createCategory(Category category);

    Category getCategoryById(Long id);

    List<Category> getAllCategories(Integer from, Integer size);

    Category updateCategory(Long id, Category category);

    void deleteCategory(Long id);

}
