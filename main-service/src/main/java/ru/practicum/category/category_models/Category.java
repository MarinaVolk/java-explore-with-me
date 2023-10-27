package ru.practicum.category.category_models;/* # parse("File Header.java")*/

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * File Name: Category.java
 * Author: Marina Volkova
 * Date: 2023-10-19,   9:05 PM (UTC+3)
 * Description:
 */

@Entity
@Table(name = "categories")
@Getter
@Setter
@ToString
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(length = 50, unique = true, nullable = false)
    private String name;
}
