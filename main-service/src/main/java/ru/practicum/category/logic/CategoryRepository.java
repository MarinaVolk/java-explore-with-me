package ru.practicum.category.logic;/* # parse("File Header.java")*/

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.category.category_models.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
}
