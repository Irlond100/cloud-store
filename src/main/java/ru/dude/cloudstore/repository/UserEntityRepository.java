package ru.dude.cloudstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dude.cloudstore.entities.User;

import java.util.Optional;

public interface UserEntityRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}