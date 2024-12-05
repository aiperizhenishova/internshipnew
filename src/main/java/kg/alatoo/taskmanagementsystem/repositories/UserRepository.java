package kg.alatoo.taskmanagementsystem.repositories;

import kg.alatoo.taskmanagementsystem.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUsername(String username);

    // Проверка наличия пользователя по email
    boolean existsByEmail(String email);

    // Проверка наличия пользователя по username
    boolean existsByUsername(String username);


    List<UserEntity> findAllByUsernameContainingIgnoreCase(String name);
}
