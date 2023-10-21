package ru.practicum.util;/* # parse("File Header.java")*/

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * File Name: Pagination.java
 * Author: Marina Volkova
 * Date: 2023-10-19,   9:15 PM (UTC+3)
 * Description:
 */
public class Pagination extends PageRequest {
    public Pagination(int page, int size, Sort sort) {
        super(page / size, size, sort);
    }
}
