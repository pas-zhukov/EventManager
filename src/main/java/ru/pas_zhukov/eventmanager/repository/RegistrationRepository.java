package ru.pas_zhukov.eventmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pas_zhukov.eventmanager.entity.EventEntity;
import ru.pas_zhukov.eventmanager.entity.RegistrationEntity;
import ru.pas_zhukov.eventmanager.entity.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Long> {
    boolean existsByUserAndEvent(UserEntity user, EventEntity event);
    Optional<RegistrationEntity> findByUserAndEvent(UserEntity user, EventEntity event);

    List<RegistrationEntity> findAllByEventId(Long id);
}
