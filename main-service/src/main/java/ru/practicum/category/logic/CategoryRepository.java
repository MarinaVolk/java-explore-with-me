package ru.practicum.category.logic;/* # parse("File Header.java")*/

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.category.category_models.Category;

/**
 * File Name: CategoryRepository.java
 * Author: Marina Volkova
 * Date: 2023-10-19,   9:07 PM (UTC+3)
 * Description:
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
}
