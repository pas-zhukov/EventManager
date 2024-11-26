package ru.pas_zhukov.eventmanager.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pas_zhukov.eventmanager.entity.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @EntityGraph(attributePaths = {"ownedEvents", "registrations"})
    Optional<UserEntity> findByLogin(String login);

    boolean existsByLogin(String login);
}
