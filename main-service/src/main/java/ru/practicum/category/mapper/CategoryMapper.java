package ru.practicum.category.mapper;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.model.Category;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryMapper {

    public static Category toCategoryEntity(NewCategoryDto newCategoryDto) {
        return new Category(
                newCategoryDto.getName()
        );
    }

    public static Category toCategoryEntity(CategoryDto categoryDto) {
        return new Category(
                categoryDto.getId(),
                categoryDto.getName()
        );
    }

    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }

    public static List<CategoryDto> toCategoryDtoList(List<Category> categoryList) {
        return categoryList.stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }
}
