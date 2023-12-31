package ru.practicum.compilations.compilations_logic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.compilations.compilations_models.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    Page<Compilation> findByPinned(Boolean pinned, PageRequest pageRequest);

    List<Compilation> findByPinned(Boolean pinned);
}
