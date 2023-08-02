package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryController {

    @Autowired
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("Входящий запрос POST /admin/categories: {}", newCategoryDto);
        final CategoryDto categoryDto = categoryService.createCategory(newCategoryDto);
        log.info("Исходящий ответ: {}", categoryDto);
        return categoryDto;
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto updateCategory(@PathVariable Long catId, @RequestBody @Valid CategoryDto categoryDto) {
        log.info("Входящий запрос PATCH /admin/categories/{}: {}", catId, categoryDto);
        final CategoryDto responseCategoryDto = categoryService.updateCategory(catId, categoryDto);
        log.info("Исходящий ответ: {}", responseCategoryDto);
        return responseCategoryDto;
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoryById(@PathVariable Long catId) {
        log.info("Входящий запрос DELETE /admin/categories/{}", catId);
        categoryService.deleteCategory(catId);
    }
}
