package ru.practicum.users.user_logic;/* # parse("File Header.java")*/

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.users.user_models.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findByIdIn(Long[] ids, PageRequest pageRequest);

    boolean existsById(Long id);

    boolean existsByEmail(String email);
}