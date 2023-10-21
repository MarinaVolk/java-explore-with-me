package ru.practicum.users;/* # parse("File Header.java")*/

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * File Name: UserRepository.java
 * Author: Marina Volkova
 * Date: 2023-10-20,   7:36 PM (UTC+3)
 * Description:
 */

public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findByIdIn(Long[] ids, PageRequest pageRequest);

    boolean existsById(Long id);

    boolean existsByEmail(String email);
}