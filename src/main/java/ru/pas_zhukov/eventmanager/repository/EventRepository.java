package ru.pas_zhukov.eventmanager.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.pas_zhukov.eventmanager.entity.EventEntity;
import ru.pas_zhukov.eventmanager.entity.UserEntity;
import ru.pas_zhukov.eventmanager.model.EventStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {

    List<EventEntity> findAllByOwnerIs(UserEntity owner);

    @Query("""
    SELECT e from EventEntity e
    JOIN FETCH UserEntity u ON e.owner = u
    JOIN FETCH LocationEntity l ON e.location = l
    INNER JOIN RegistrationEntity r ON r.event.id = e.id
    WHERE r.user.id = :userId
    """)
    List<EventEntity> findUserRegisteredEvents(@Param("userId") Long userId);

    void removeEventEntityById(Long id);

    @Query("""
    SELECT e FROM EventEntity e
    WHERE (:name IS NULL OR e.name LIKE %:name%)
    AND (:placesMin IS NULL OR e.maxPlaces >= :placesMin)
    AND (:placesMax IS NULL OR e.maxPlaces <= :placesMax)
    AND (CAST(:dateStartAfter as date) IS NULL OR e.date >= :dateStartAfter)
    AND (CAST(:dateStartBefore as date) IS NULL OR e.date <= :dateStartBefore)
    AND (:costMin IS NULL OR e.cost >= :costMin)
    AND (:costMax IS NULL OR e.cost <= :costMax)
    AND (:durationMin IS NULL OR e.duration >= :durationMin)
    AND (:durationMax IS NULL OR e.duration <= :durationMax)
    AND (:locationId IS NULL OR e.location.id = :locationId)
    AND (:eventStatus IS NULL OR e.status = :eventStatus)
    """)
    List<EventEntity> searchEvents(
            @Param("name") String name,
            @Param("placesMin") Integer placesMin,
            @Param("placesMax") Integer placesMax,
            @Param("dateStartAfter") LocalDateTime dateStartAfter,
            @Param("dateStartBefore") LocalDateTime dateStartBefore,
            @Param("costMin") BigDecimal costMin,
            @Param("costMax") BigDecimal costMax,
            @Param("durationMin") Integer durationMin,
            @Param("durationMax") Integer durationMax,
            @Param("locationId") Long locationId,
            @Param("eventStatus") EventStatus eventStatus
    );

    @Modifying
    @Transactional
    @Query("UPDATE EventEntity e SET e.status = :eventStatus WHERE e.id IN :ids")
    void changeEventsStatus(@Param("ids") List<Long> ids, @Param("eventStatus") EventStatus eventStatus);

    @Modifying
    @Transactional
    @Query("UPDATE EventEntity e SET e.occupiedPlaces = e.occupiedPlaces + 1 WHERE e.id = :id")
    void increaseOccupiedPlacesByEventId(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE EventEntity e SET e.occupiedPlaces = e.occupiedPlaces - 1 WHERE e.id = :id")
    void decreaseOccupiedPlacesByEventId(@Param("id") Long id);

    @Query("SELECT e FROM EventEntity e WHERE e.status = :status AND current_timestamp >= e.date AND current_timestamp < e.date + (e.duration MINUTE)")
    @EntityGraph(attributePaths = {"owner", "registrations"})
    List<EventEntity> findAllStartedEventsByStatus(@Param("status") EventStatus status);

    @Query("SELECT e FROM EventEntity e WHERE e.status = :status AND current_timestamp >= e.date + (e.duration MINUTE)")
    @EntityGraph(attributePaths = {"owner", "registrations"})
    List<EventEntity> findAllFinishedEventsByStatus(@Param("status") EventStatus status);

}
