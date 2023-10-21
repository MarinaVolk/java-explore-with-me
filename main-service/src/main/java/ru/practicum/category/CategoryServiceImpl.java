package ru.practicum.category;/* # parse("File Header.java")*/

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exceptions.NotAvailableException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.util.Pagination;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.category.CategoryMapper.toCategory;
import static ru.practicum.category.CategoryMapper.toCategoryDto;

/**
 * File Name: CategoryServiceImpl.java
 * Author: Marina Volkova
 * Date: 2023-10-19,   9:09 PM (UTC+3)
 * Description:
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    @Override
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        Category category = categoryRepository.save(toCategory(newCategoryDto));
        log.info("Создание категории {}", category);
        return toCategoryDto(category);
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Категория с id = " + id + " не найдена."));
        log.info("Получение категории с id = {}.", id);
        return toCategoryDto(category);
    }

    @Transactional
    @Override
    public CategoryDto updateCategoryById(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Категория с id = " + id + " не найдена."));
        category.setName(categoryDto.getName());
        log.info("Категория с id = {} обновлена.", category.getId());
        return toCategoryDto(categoryRepository.save(category));
    }

    @Transactional(readOnly = true)
    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        log.info("Получены все категории, from = {}, size = {}", from, size);
        return categoryRepository.findAll(new Pagination(from, size, Sort.unsorted())).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteCategoryById(Long id) {
        boolean isExist = categoryRepository.existsById(id);
        if (!isExist) {
            throw new NotFoundException("Категория с id = " + id + " не найдена.");
        } else {
            try {
                categoryRepository.deleteById(id);
            } catch (RuntimeException e) {
                throw new NotAvailableException("Категория не удалена.");
            }
            log.info("Категория с id = {} удалена.", id);
        }
    }
}
