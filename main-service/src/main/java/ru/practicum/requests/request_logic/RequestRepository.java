package ru.practicum.requests.request_logic;/* # parse("File Header.java")*/

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.requests.request_models.Request;

import java.util.List;

/**
 * File Name: RequestRepository.java
 * Author: Marina Volkova
 * Date: 2023-10-20,   7:46 PM (UTC+3)
 * Description:
 */
public interface RequestRepository extends JpaRepository<Request, Long> {
    boolean existsByRequesterAndEvent(Long requesterId, Long eventId);

    List<Request> findByRequester(Long requesterId);

    List<Request> findByEvent(Long eventId);

    List<Request> findByIdIn(List<Long> ids);

    @Modifying
    @Query(value = "UPDATE Request " +
            "SET status = :status " +
            "WHERE id IN (:ids)")
    void setStatus(List<Long> ids, String status);

    List<Request> findByEventAndIdIn(Long eventId, List<Long> ids);
}
