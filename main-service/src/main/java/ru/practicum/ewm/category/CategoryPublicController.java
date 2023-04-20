package ru.practicum.ewm.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.service.CategoryService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CategoryPublicController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable Long catId, HttpServletRequest request) {
        log.debug("{} request {} received", request.getMethod(), request.getRequestURI());
        return categoryMapper.toCategoryDto(categoryService.getCategoryById(catId));
    }

    @GetMapping
    public List<CategoryDto> getAllCategories(@RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                              @RequestParam(defaultValue = "10") @Positive Integer size,
                                              HttpServletRequest request) {
        String params = request.getQueryString();
        if (params != null) {
            log.debug("{} request {}?{} received", request.getMethod(), request.getRequestURI(), params);
        } else {
            log.debug("{} request {} received", request.getMethod(), request.getRequestURI());
        }
        return categoryMapper.toCategoryDtoList(categoryService.getAllCategories(from, size));
    }

}
