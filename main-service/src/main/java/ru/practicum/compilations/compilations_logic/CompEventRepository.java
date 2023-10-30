package ru.practicum.compilations.compilations_logic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import ru.practicum.compilations.compilations_models.CompEvent;

import java.util.List;

public interface CompEventRepository extends JpaRepository<CompEvent, Long> {
    List<CompEvent> findAllByCompId(Long compId);

    List<CompEvent> findAllByCompIdIn(List<Long> compIds);

    @Modifying
    void deleteByCompId(Long compId);
}
