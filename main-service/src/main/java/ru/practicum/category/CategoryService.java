package ru.practicum.category;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(NewCategoryDto newCategoryDto);

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategoryById(Long categoryId);

    CategoryDto updateCategoryById(Long id, CategoryDto categoryDto);

    void deleteCategoryById(Long id);
}
