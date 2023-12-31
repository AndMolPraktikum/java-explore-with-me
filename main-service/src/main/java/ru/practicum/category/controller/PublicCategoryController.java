package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
@Slf4j
public class PublicCategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> getAllCategories(@RequestParam(defaultValue = "0") int from,
                                              @RequestParam(defaultValue = "10") int size) {
        log.info("Входящий запрос GET /categories?from={}&size={}", from, size);
        final List<CategoryDto> categoryDtoList = categoryService.getCategories(from, size);
        log.info("Исходящий ответ: {}", categoryDtoList);
        return categoryDtoList;
    }

    @GetMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getCategoryById(@PathVariable long catId) {
        log.info("Входящий запрос GET /categories/{}", catId);
        final CategoryDto categoryDto = categoryService.getCategoryDtoById(catId);
        log.info("Исходящий ответ: {}", categoryDto);
        return categoryDto;
    }
}
