package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.exception.CategoryNotFoundException;
import ru.practicum.exception.CategoryUsedConflictException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        Pageable page = PageRequest.of(from, size, Sort.by("id"));
        List<Category> categoryList = categoryRepository.findAll(page).getContent();
        return CategoryMapper.toCategoryDtoList(categoryList);
    }

    @Override
    public Category getCategoryById(long catId) {
        Optional<Category> categoryOptional = categoryRepository.findById(catId);
        if (categoryOptional.isEmpty()) {
            log.error("Category with id={} was not found", catId);
            throw new CategoryNotFoundException(String.format("Category with id=%d was not found", catId));
        }
        return categoryOptional.get();
    }

    @Override
    public CategoryDto getCategoryDtoById(long catId) {
        return CategoryMapper.toCategoryDto(getCategoryById(catId));
    }

    @Override
    @Transactional
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.toCategoryEntity(newCategoryDto);
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {
        Category category = getCategoryById(catId);
        if (categoryDto.getName() != null) {
            category.setName(categoryDto.getName());
        }
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {
        if (categoryRepository.isUsed(catId)) {
            log.error("The category is not empty");
            throw new CategoryUsedConflictException("The category is not empty");
        }
        categoryRepository.deleteById(catId);
    }
}
