package ru.practicum.category;/* # parse("File Header.java")*/

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * File Name: CategoryMapper.java
 * Author: Marina Volkova
 * Date: 2023-10-19,   8:56 PM (UTC+3)
 * Description:
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryMapper {
    public static CategoryDto toCategoryDto(Category category) {

        return new CategoryDto(category.getId(), category.getName());
    }

    public static List<CategoryDto> toCategoriesDto(Iterable<Category> categories) {

        List<CategoryDto> listToReturn = new ArrayList<>();

        for (Category category : categories) {
            listToReturn.add(toCategoryDto(category));
        }
        return listToReturn;
    }

    public static Category toCategory(NewCategoryDto newCategoryDto) {
        Category category = new Category();
        category.setName(newCategoryDto.getName());
        return category;
    }

    /*public static Category toCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        return category;
    }*/
}
