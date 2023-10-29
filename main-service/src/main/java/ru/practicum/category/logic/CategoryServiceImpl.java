package ru.practicum.category.logic;/* # parse("File Header.java")*/

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.category_models.Category;
import ru.practicum.category.category_models.CategoryDto;
import ru.practicum.category.category_models.CategoryMapper;
import ru.practicum.category.category_models.NewCategoryDto;
import ru.practicum.events.event_logic.EventRepository;
import ru.practicum.exceptions.NotAvailableException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.util.Pagination;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.category.category_models.CategoryMapper.toCategory;
import static ru.practicum.category.category_models.CategoryMapper.toCategoryDto;


@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;


    @Transactional
    @Override
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        if (categoryRepository.existsByName(newCategoryDto.getName())) {
            //&& !category.getName().equals(newCategoryDto.getName())) {
            throw new NotAvailableException("Такое имя категории уже есть в базе.");
        }
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

        if (categoryRepository.existsByName(categoryDto.getName())
                && !category.getName().equals(categoryDto.getName())) {
            throw new NotAvailableException("Такое имя категории уже есть в базе.");
        }

        category.setName(categoryDto.getName());
        CategoryDto categoryDtoToReturn = CategoryMapper.toCategoryDto(categoryRepository.save(category));
        log.info("Категория с id = {} обновлена.", category.getId());
        return categoryDtoToReturn;
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
        boolean exists = categoryRepository.existsById(id);
        if (!exists) {
            throw new NotFoundException("Категория с id = " + id + " не найдена.");
        }
        if (eventRepository.existsByCategoryId(id)) {
            throw new NotAvailableException("Категория с id = " + id + " не может быть удалена по причине наличия событий данной категории.");
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
