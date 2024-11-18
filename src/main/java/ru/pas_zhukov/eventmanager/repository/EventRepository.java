package ru.pas_zhukov.eventmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pas_zhukov.eventmanager.entity.EventEntity;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Integer> {
}
