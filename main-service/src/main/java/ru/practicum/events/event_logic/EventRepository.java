package ru.practicum.events.event_logic;/* # parse("File Header.java")*/

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.events.event_models.Event;

import java.util.List;
import java.util.Optional;

/**
 * File Name: EventRepository.java
 * Author: Marina Volkova
 * Date: 2023-10-20,   7:25 PM (UTC+3)
 * Description:
 */
public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    @Query("SELECT e " +
            "FROM Event AS e " +
            "WHERE e.initiator.id = :initiatorId")
    Page<Event> findByInitiatorId(Long initiatorId, Pageable pageable);

    Optional<Event> findByIdAndState(Long eventId, String state);

    List<Event> findAllByIdIn(Long[] ids);

    boolean existsByCategoryId(Long categoryId);

    @Modifying
    @Query("UPDATE Event " +
            "SET confirmed_requests = confirmed_requests + 1 " +
            "WHERE id = :id")
    void updateConfirmedRequestsById(Long id);

}
